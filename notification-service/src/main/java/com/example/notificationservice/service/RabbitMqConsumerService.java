package com.example.notificationservice.service;

import com.example.notificationservice.dto.BatteryEvent;
import com.example.notificationservice.dto.HeartbeatEvent;
import com.example.notificationservice.dto.MessageType;
import com.example.notificationservice.dto.NotificationEvent;
import com.example.notificationservice.dto.OxygenEvent;
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
        getChildName(event.childId())
                .flatMapMany(childName ->
                        getUsersId(event.childId())
                                .flatMap(parentId ->
                                        redisService.publish(
                                                parentId.toString(),
                                                childName + "has abnorml speed " + event.speed(),
                                                MessageType.ALERT
                                                )
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
                .flatMapMany(childName ->
                        getUsersId(event.childId())
                                .flatMap(parentId ->
                                        redisService.publish(
                                                parentId.toString(),
                                                childName + " is " + event.eventType() + " for " + event.safezoneName(),
                                                MessageType.INFO
                                        )
                                )
                )
                .then()
                .doOnError(error -> log.error("Error consuming speed alert", error))
                .subscribe();
    }

    @RabbitListener(queues = "${app.rabbitmq.queues.heartbeat}")
    public void consumeHeartbeatAlert(HeartbeatEvent event) {
        log.info("Received abnormal heartbeat event: {}", event);
        getChildName(Long.valueOf(event.childId()))
                .flatMapMany(childName ->
                        getUsersId(Long.valueOf(event.childId()))
                                .flatMap(parentId ->
                                        redisService.publish(
                                                parentId.toString(),
                                                childName + " has abnormal heartbeat: " + event.heartbeats() + " bpm",
                                                MessageType.ALERT
                                        )
                                )
                )
                .then()
                .doOnError(error -> log.error("Error consuming heartbeat alert", error))
                .subscribe();
    }

    @RabbitListener(queues = "${app.rabbitmq.queues.oxygen}")
    public void consumeOxygenAlert(OxygenEvent event) {
        log.info("Received abnormal oxygen event: {}", event);
        getChildName(Long.valueOf(event.childId()))
                .flatMapMany(childName ->
                        getUsersId(Long.valueOf(event.childId()))
                                .flatMap(parentId ->
                                        redisService.publish(
                                                parentId.toString(),
                                                childName + " has low oxygen level: " + event.oxygenLevel() + "%",
                                                MessageType.ALERT
                                        )
                                )
                )
                .then()
                .doOnError(error -> log.error("Error consuming oxygen alert", error))
                .subscribe();
    }

    @RabbitListener(queues = "${app.rabbitmq.queues.battery}")
    public void consumeBatteryAlert(BatteryEvent event) {
        log.info("Received abnormal battery event: {}", event);
        getChildName(Long.valueOf(event.childId()))
                .flatMapMany(childName ->
                        getUsersId(Long.valueOf(event.childId()))
                                .flatMap(parentId ->
                                        redisService.publish(
                                                parentId.toString(),
                                                childName + " has low battery: " + event.batteryLevel() + "%",
                                                MessageType.ALERT
                                        )
                                )
                )
                .then()
                .doOnError(error -> log.error("Error consuming battery alert", error))
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
