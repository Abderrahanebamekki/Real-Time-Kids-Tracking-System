package com.example.dailytrackingservice.repositories;

import com.example.dailytrackingservice.entities.Vitals;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.Instant;

@Repository
public interface VitalsRepository extends R2dbcRepository<Vitals, Long> {

    Flux<Vitals> findByChildIdAndTimestampBetween(String childId, Instant start, Instant end);
}
