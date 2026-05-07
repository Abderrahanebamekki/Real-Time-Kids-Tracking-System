package com.example.notificationservice.service;

import com.example.notificationservice.dto.MessageType;
import com.example.notificationservice.dto.NotificationEvent;
import com.example.notificationservice.dto.SafeZoneEvent;
import com.example.notificationservice.dto.SpeedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitMqConsumerService {

    private final grpcClient grpcClient;
    private final RedisService redisService;

    @RabbitListener(queues = "${app.rabbitmq.queues.speed}")
    public void consumeSpeedAlert(SpeedEvent event) {
        log.info("event {}", event.speed());
        getChildName(event.childId())
                .flatMap(childName ->
                        redisService.saveNotification(
                                childName + " has abnormal speed " + event.speed(),
                                MessageType.ALERT
                        )
                )
                .flatMapMany(messageId ->
                        getUsersId(event.childId())
                                .flatMap(parentId ->
                                        redisService.saveNotificationForParent(parentId, messageId)
                                )
                )
                .then()
                .doOnError(error -> log.error("Error consuming speed alert", error))
                .subscribe();
    }

    @RabbitListener(queues = "${app.rabbitmq.queues.safezone}")
    public void consumeSafeZoneAlert(SafeZoneEvent event) {
        log.info("message {}" , event.safezoneName());
        getChildName(event.childId())
                .flatMap(childName ->
                        redisService.saveNotification(
                                childName + " is " + event.eventType() + " for " + event.safezoneName(),
                                MessageType.INFO
                        )
                )
                .flatMapMany(messageId ->
                        getUsersId(event.childId())
                                .flatMap(parentId ->
                                        redisService.saveNotificationForParent(parentId, messageId)
                                )
                )
                .then()
                .doOnError(error -> log.error("Error consuming speed alert", error))
                .subscribe();
    }

    private Mono<String> getChildName(Long childId) {
        return redisService.getChildName(childId)
                .switchIfEmpty(
                        grpcClient.getChildName(childId)
                                .flatMap(childName ->
                                        redisService.saveChildName(childId, childName)
                                                .thenReturn(childName)
                                )
                );
    }

    private Flux<Long> getUsersId(Long childId) {
        return redisService.getUserIdsForChild(childId)
                .switchIfEmpty(
                        grpcClient.getUsersId(childId)
                                .collectList()
                                .flatMapMany(userIds ->
                                        redisService.saveUserSIdForChild(childId, userIds)
                                                .thenMany(Flux.fromIterable(userIds))
                                )
                );
    }
}
