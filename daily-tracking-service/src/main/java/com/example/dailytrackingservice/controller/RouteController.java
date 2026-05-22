package com.example.dailytrackingservice.controller;

import com.example.dailytrackingservice.entities.GpsLog;
import com.example.dailytrackingservice.service.DailyRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.*;

@RestController
@RequestMapping("/daily_tracking/route")
@RequiredArgsConstructor
public class RouteController {

    private final DailyRouteService dailyRouteService;

    @GetMapping("/{childId}")
    public Flux<GpsLog> getRouteForChild(
            @PathVariable String childId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        LocalDate targetDate = date != null ? date : LocalDate.now(ZoneOffset.UTC);
        Instant start = targetDate.atTime(LocalTime.of(18, 0)).atZone(ZoneOffset.UTC).toInstant();
        Instant end = targetDate.atTime(LocalTime.MIDNIGHT).plusDays(1).atZone(ZoneOffset.UTC).toInstant();
        return dailyRouteService.getRouteForChildInTimeRange(childId, start, end);
    }
}
