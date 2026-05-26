package com.example.dailytrackingservice.controller;

import com.example.dailytrackingservice.entities.DailyRoute;
import com.example.dailytrackingservice.entities.GpsLog;
import com.example.dailytrackingservice.service.DailyRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
        return dailyRouteService.getRouteForChild(childId, date);
    }

}
