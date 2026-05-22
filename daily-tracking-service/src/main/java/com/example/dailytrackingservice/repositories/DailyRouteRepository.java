package com.example.dailytrackingservice.repositories;

import com.example.dailytrackingservice.entities.DailyRoute;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Repository
public interface DailyRouteRepository extends R2dbcRepository<DailyRoute, Long> {

    Mono<DailyRoute> findByChildIdAndDate(String childId, LocalDate date);
}
