package com.example.dailytrackingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    public Mono<String> getChildId(String deviceId) {
        return reactiveRedisTemplate.opsForValue().get(deviceId);
    }
}
