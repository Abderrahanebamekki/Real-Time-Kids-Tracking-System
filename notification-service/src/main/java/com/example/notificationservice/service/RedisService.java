package com.example.notificationservice.service;

import com.example.notificationservice.dto.MessageType;
import com.example.notificationservice.dto.NotificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final ReactiveRedisTemplate<String , NotificationEvent> redisTemplateNotification;
    private final ReactiveStringRedisTemplate stringRedisTemplate;
    private final ReactiveRedisMessageListenerContainer listenerContainer;
    private final ObjectMapper objectMapper;




    public Mono<Void> saveUserSIdForChild(Long childId, List<Long> userId) {
        String key = "ch:" + childId+":ps";
       log.info("Saving user ids for child: {} , user ids: {}", childId, userId);
        Set<ZSetOperations.TypedTuple<String>> tuples = userId.stream()
                .map(id -> ZSetOperations.TypedTuple.of(
                        String.valueOf(id),
                        (double) System.currentTimeMillis()  // score = timestamp
                ))
                .collect(Collectors.toSet());

        return stringRedisTemplate.opsForZSet()
                .addAll(key, tuples)
                .then();
    }

    public Flux<Long> getUserIdsForChild(Long childId) {
        String key = "ch:" + childId+":ps";
        return stringRedisTemplate.opsForZSet()
                .range(key, Range.unbounded())
                .map(Long::parseLong);
    }
    public Mono<Void> saveChildName(Long childId, String childName) {
        return stringRedisTemplate.opsForValue()
                .set("ch:"+ childId.toString() + ":n", childName)
                .then();
    }

    public Mono<String> getChildName(Long childId) {
        return stringRedisTemplate.opsForValue()
                .get("ch:"+ childId.toString() + ":n");
    }

    public Mono<String> saveNotification(String message , MessageType messageType) {
        String key = UUID.randomUUID().toString();
        NotificationEvent notificationDto = NotificationEvent.builder()
                .message(message)
                .dateTime(LocalDateTime.now())
                .type(messageType)
                .build();
        return redisTemplateNotification.opsForValue()
                .set(key, notificationDto)
                .thenReturn(key);
    }

    public Mono<NotificationEvent> getNotification(String messageId) {
        return redisTemplateNotification.opsForValue()
                .get(messageId);
    }
    private String streamKey(String userId) {
        return "notification-stream:" + userId;
    }

    public Mono<Void> publish(String userId , String message , MessageType messageType){
        NotificationEvent event = NotificationEvent.builder()
                .message(message)
                .dateTime(LocalDateTime.now())
                .type(messageType)
                .build();
        String streamKey = streamKey(userId);
        Map<String, NotificationEvent> body = Map.of("data", event);
        return redisTemplateNotification.opsForStream()
                .add(StreamRecords.mapBacked(body).withStreamKey(streamKey))
                .flatMap(recordId ->
                        redisTemplateNotification.expire(streamKey , Duration.ofDays(7))
                )
                .then();
    }

    public Flux<NotificationEvent> subscribe(String parentId) {
        String streamKey = streamKey(parentId);
        AtomicReference<String> lastId = new AtomicReference<>("0");

        return Flux.interval(Duration.ofSeconds(2))
                .flatMap(tick ->
                        redisTemplateNotification.opsForStream()
                                .read(
                                        NotificationEvent.class,
                                        StreamReadOptions.empty().count(10),
                                        StreamOffset.create(streamKey, ReadOffset.from(lastId.get()))
                                )
                                .flatMap(record ->
                                        redisTemplateNotification.opsForStream()
                                                .delete(streamKey, record.getId())
                                                .doOnSuccess(deleted ->
                                                        lastId.set(record.getId().getValue())
                                                )
                                                .thenReturn(record.getValue())
                                )
                );
    }
}