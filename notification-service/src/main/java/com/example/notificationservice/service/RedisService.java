package com.example.notificationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final ReactiveStringRedisTemplate redisTemplate;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // =========================
    // USERS
    // =========================

    public Mono<Void> saveUsersId(Long childId, List<Long> usersId) {
        String key = usersKey(childId);

        return redisTemplate.delete(key)
                .thenMany(
                        Flux.fromIterable(usersId)
                                .map(String::valueOf)
                                .flatMap(userId ->
                                        redisTemplate.opsForList().rightPush(key, userId)
                                )
                )
                .then();
    }

    public Flux<Long> getUsersId(Long childId) {
        return redisTemplate.opsForList()
                .range(usersKey(childId), 0, -1)
                .map(Long::valueOf);
    }

    // =========================
    // SPEED MESSAGES (LIST)
    // =========================

    public Mono<Void> saveSpeedMessage(Long childId, Object message) {
        return saveUnreadMessage(speedMessagesKey(childId), message);
    }

    public Flux<String> getUnreadSpeedMessages(Long childId) {
        return popAllUnreadMessages(speedMessagesKey(childId));
    }

    // =========================
    // SAFEZONE MESSAGES (LIST)
    // =========================

    public Mono<Void> saveSafeZoneMessage(Long childId, Object message) {
        return saveUnreadMessage(safeZoneMessagesKey(childId), message);
    }

    public Flux<String> getUnreadSafeZoneMessages(Long childId) {
        return popAllUnreadMessages(safeZoneMessagesKey(childId));
    }

    // =========================
    // INTERNAL HELPERS
    // =========================

    private Mono<Void> saveUnreadMessage(String key, Object message) {
        String json = toJson(message);

        return redisTemplate.opsForList()
                .rightPush(key, json)   // append to list
                .then();
    }

    /**
     * Pop all unread messages (FIFO) and remove them from Redis
     */
    private Flux<String> popAllUnreadMessages(String key) {
        return Flux.defer(() -> redisTemplate.opsForList().leftPop(key))
                .repeat()
                .takeWhile(message -> message != null);
    }

    // =========================
    // KEYS
    // =========================

    private String usersKey(Long childId) {
        return "child:" + childId + ":users";
    }

    private String speedMessagesKey(Long childId) {
        return "ch:" + childId + ":m:speed";
    }

    private String safeZoneMessagesKey(Long childId) {
        return "ch:" + childId + ":m:safezone";
    }

    // =========================
    // JSON
    // =========================

    private String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }
}