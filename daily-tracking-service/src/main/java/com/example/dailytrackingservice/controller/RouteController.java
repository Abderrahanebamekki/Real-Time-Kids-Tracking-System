package com.example.dailytrackingservice.controller;

import com.example.dailytrackingservice.entities.DailyRoute;
import com.example.dailytrackingservice.service.DailyRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.*;

@RestController
@RequestMapping("/daily_tracking/route")
@RequiredArgsConstructor
public class RouteController {

    private final DailyRouteService dailyRouteService;

    @GetMapping("/{childId}")
    public Mono<DailyRoute> getRouteForChild(
            @PathVariable String childId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        LocalTime now = LocalTime.now(ZoneOffset.UTC);
        if (now.isBefore(LocalTime.of(18, 0))) {
            return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Route data is only available from 6 PM"));
        }

        LocalDate targetDate = date != null ? date : LocalDate.now(ZoneOffset.UTC);
        return dailyRouteService.getRouteForChild(childId, targetDate)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "No route found for this date")));
    }
}
