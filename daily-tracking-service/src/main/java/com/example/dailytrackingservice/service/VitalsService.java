package com.example.dailytrackingservice.service;

import com.example.dailytrackingservice.entities.Vitals;
import com.example.dailytrackingservice.repositories.VitalsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
@Slf4j
public class VitalsService {

    private final VitalsRepository vitalsRepository;

    public Flux<Vitals> getVitalsForChildInDay(String childId, LocalDate day) {
        if (day == null) {
            day = LocalDate.now(ZoneOffset.UTC);
        }
        Instant start = day.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant end = day.atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant();
        return vitalsRepository.findByChildIdAndTimestampBetween(childId, start, end);
    }
}
