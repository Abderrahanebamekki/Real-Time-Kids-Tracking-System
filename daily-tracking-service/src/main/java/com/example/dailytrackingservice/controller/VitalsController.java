package com.example.dailytrackingservice.controller;

import com.example.dailytrackingservice.entities.Vitals;
import com.example.dailytrackingservice.dto.VitalEvent;
import com.example.dailytrackingservice.service.RedisService;
import com.example.dailytrackingservice.service.VitalsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@RestController
@RequestMapping("/daily_tracking/vitals")
@RequiredArgsConstructor
public class VitalsController {

    private final RedisService redisService;
    private final VitalsService vitalsService;

    @GetMapping(value = "/subscribe/{childId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<VitalEvent> subscribeToVitals(@PathVariable Long childId) {
        return redisService.subscribeToVitals(childId);
    }

    @GetMapping("/{childId}")
    public Flux<Vitals> getAllVitalsForChildInDay(
            @PathVariable String childId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day) {
        return vitalsService.getVitalsForChildInDay(childId, day);
    }
}
