package com.example.geofencingservice.safe_zone;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalTime;

@Repository
public interface SafeZoneRepository extends R2dbcRepository<SafeZoneEntity,Long> {
    @Query("""
    INSERT INTO safezone (
        child_id,
        name,
        radius_meters,
        center
    )
    VALUES (
        :childId,
        :name,
        :radiusMeters,
        ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography
    )
""")
    Mono<Void> insertSafeZone(
            Long childId,
            String name,
            Double radiusMeters,
            Double longitude,
            Double latitude
    );

    @Query("""
    SELECT id,
        name,
        radius_meters As radius,
        ST_Y(center::geometry) AS latitude,
        ST_X(center::geometry) AS longitude
    FROM safezone
    WHERE child_id = :childId
""")
    Flux<SafeZoneEntity> findByChildId(Long childId);


    @Query("""
    SELECT name
    FROM safezone
    WHERE child_id = :childId
    AND ST_DWithin(
        center,
        ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography,
        radius_meters
    )
""")
    Mono<String> findZoneNamesContainingLocation(
            Long childId,
            Double longitude,
            Double latitude
    );

    Mono<Void> deleteByChildId(Long childId);

}
