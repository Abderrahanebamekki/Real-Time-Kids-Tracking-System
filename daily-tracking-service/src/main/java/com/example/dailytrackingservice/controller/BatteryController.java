package com.example.dailytrackingservice.controller;

import com.example.dailytrackingservice.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/daily_tracking/battery")
@RequiredArgsConstructor
public class BatteryController {

    private final RedisService redisService;

    @GetMapping("/{childId}")
    public Mono<String> getBattery(@PathVariable String childId) {
        return redisService.getBattery(childId);
    }
}
