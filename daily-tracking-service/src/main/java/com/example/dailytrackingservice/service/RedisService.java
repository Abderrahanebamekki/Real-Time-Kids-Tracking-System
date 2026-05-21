package com.example.dailytrackingservice.service;

import com.example.dailytrackingservice.dto.VitalEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
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

    public Mono<Void> publishVitals(String childId, VitalEvent vitalEvent) {
        String channel = buildChannel(childId);
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

    public Flux<VitalEvent> subscribeToVitals(Long childId) {
        String channel = buildChannel(childId.toString());
        return reactiveRedisTemplate.listenToChannel(channel)
                .map(message -> {
                    try {
                        return objectMapper.readValue(message.getMessage(), VitalEvent.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .doOnNext(vitalEvent -> log.info("Received vitals on channel {}: {}", channel, vitalEvent))
                .doOnError(error -> log.error("Error on channel {}: {}", channel, error.getMessage()));
    }

    private Mono<String> toJson(VitalEvent event) {
        try {
            return Mono.just(objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }

    private String buildChannel(String childId) {
        return "ch:" + childId + ":v";
    }
}
