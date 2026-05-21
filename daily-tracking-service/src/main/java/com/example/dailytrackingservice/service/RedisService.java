package com.example.dailytrackingservice.service;

import com.example.dailytrackingservice.dto.VitalEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {

    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;
    private final ObjectMapper objectMapper;

    public Mono<String> getChildId(String deviceId) {
        return reactiveRedisTemplate.opsForValue().get(deviceId);
    }

    public Mono<Void> publishVitals(String deviceId, VitalEvent vitalEvent) {
        String channel = buildChannel(deviceId);
        return toJson(vitalEvent)
                .flatMap(json -> reactiveRedisTemplate.convertAndSend(channel, json))
                .then();
    }

    public Mono<Void> saveVitalsToRedis(String deviceId, VitalEvent vitalEvent) {
        String key = buildChannel(deviceId);
        return toJson(vitalEvent)
                .flatMap(json -> reactiveRedisTemplate.opsForValue().set(key, json))
                .then();
    }

    public void subscribeToVitalsChannel(String deviceId) {
        String channel = buildChannel(deviceId);
        reactiveRedisTemplate.listenToChannel(channel)
                .doOnNext(message -> log.info("Received message on channel {}: {}", channel, message.getMessage()))
                .doOnError(error -> log.error("Error on channel {}: {}", channel, error.getMessage()))
                .subscribe();
    }

    private Mono<String> toJson(VitalEvent event) {
        try {
            return Mono.just(objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }

    private String buildChannel(String deviceId) {
        return "ch:" + deviceId + ":v";
    }
}
