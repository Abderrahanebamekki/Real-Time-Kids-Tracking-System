package com.example.geofencingservice.service;


import com.example.geofencingservice.dto.SafeZoneRequest;
import com.example.geofencingservice.dto.SafeZoneResponse;
import com.example.geofencingservice.safe_zone.SafeZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.support.LoggingProducerListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SafeZoneService {

    private final SafeZoneRepository safeZoneRepository;
    private final LoggingProducerListener loggingProducerListener;

    public Mono<Void> createSafeZone(SafeZoneRequest safeZone) {
        return safeZoneRepository.insertSafeZone(
                safeZone.childId(),
                safeZone.name(),
                safeZone.radius(),
                safeZone.longitude(),
                safeZone.latitude()
        );
    }

    public Flux<SafeZoneResponse> getAllSafeZoneByChild(Long childId){
        return safeZoneRepository.findByChildId(childId)
                .map( safeZone -> SafeZoneResponse.builder()
                        .id(safeZone.getId())
                        .name(safeZone.getName())
                        .latitude(safeZone.getLatitude())
                        .longitude(safeZone.getLongitude())
                        .radius(safeZone.getRadius())
                        .build()
                );
    }

    public Mono<Void> deleteSafeZone(Long id){
        return safeZoneRepository.deleteById(id);
    }


}
