package com.example.dailytrackingservice.service;

import com.example.dailytrackingservice.entities.GpsLog;
import com.example.dailytrackingservice.repositories.GpsLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyRouteService {

    private final GpsLogRepository gpsLogRepository;

    @Scheduled(cron = "0 30 17 * * *")
    public void processDailyRoutes() {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        Instant startOfDay = today.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endOfDay = today.atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant();

        gpsLogRepository.findByChildIdAndTimestampBetween(null, startOfDay, endOfDay)
                .collectList()
                .doOnSuccess(points -> log.info("Daily route processing completed for {}. Total points: {}", today, points.size()))
                .doOnError(error -> log.error("Error processing daily routes: {}", error.getMessage()))
                .subscribe();
    }

    public Flux<GpsLog> getRouteForChildInTimeRange(String childId, Instant start, Instant end) {
        return gpsLogRepository.findByChildIdAndTimestampBetween(childId, start, end);
    }
}
