package com.example.dailytrackingservice.controller;

import com.example.dailytrackingservice.dto.VitalEvent;
import com.example.dailytrackingservice.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/daily_tracking/vitals")
@RequiredArgsConstructor
public class VitalsController {

    private final RedisService redisService;

    @GetMapping(value = "/subscribe/{childId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<VitalEvent> subscribeToVitals(@PathVariable Long childId) {
        return redisService.subscribeToVitals(childId);
    }
}
