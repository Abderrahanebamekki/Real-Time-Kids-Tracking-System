package com.example.dailytrackingservice.repositories;

import com.example.dailytrackingservice.entities.GpsLog;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.Instant;

@Repository
public interface GpsLogRepository extends R2dbcRepository<GpsLog, Long> {

    Flux<GpsLog> findByChildIdAndTimestampBetween(String childId, Instant start, Instant end);
}
