package com.example.dailytrackingservice.service;

import com.example.dailytrackingservice.dto.GPS;
import com.example.dailytrackingservice.entities.DailyRoute;
import com.example.dailytrackingservice.entities.GpsLog;
import com.example.dailytrackingservice.repositories.DailyRouteRepository;
import com.example.dailytrackingservice.repositories.GpsLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyRouteService {

    private final GpsLogRepository gpsLogRepository;
    private final DailyRouteRepository dailyRouteRepository;
    private final ObjectMapper objectMapper;

    @Scheduled(cron = "0 30 17 * * *")
    public void processDailyRoutes() {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        Instant startOfDay = today.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endOfDay = today.atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant();

        gpsLogRepository.findByChildIdAndTimestampBetween(null, startOfDay, endOfDay)
                .collectList()
                .flatMapMany(points -> {
                    Map<String, List<GpsLog>> grouped = points.stream()
                            .collect(Collectors.groupingBy(GpsLog::getChildId));
                    return Flux.fromIterable(grouped.entrySet());
                })
                .flatMap(entry -> {
                    String childId = entry.getKey();
                    List<GPS> routePoints = entry.getValue().stream()
                            .map(p -> new GPS(p.getLongitude(), p.getLatitude(), p.getSpeed()))
                            .toList();
                    try {
                        String json = objectMapper.writeValueAsString(routePoints);
                        DailyRoute route = DailyRoute.builder()
                                .childId(childId)
                                .date(today)
                                .routePoints(json)
                                .build();
                        return dailyRouteRepository.save(route);
                    } catch (JsonProcessingException e) {
                        log.error("Failed to serialize route points for child {}", childId, e);
                        return Mono.empty();
                    }
                })
                .doOnComplete(() -> log.info("Daily route processing completed for {}", today))
                .doOnError(error -> log.error("Error processing daily routes", error))
                .subscribe();
    }

    public Mono<DailyRoute> getRouteForChild(String childId, LocalDate date) {
        return dailyRouteRepository.findByChildIdAndDate(childId, date);
    }
}
