package com.example.geofencingservice.service;

import com.example.geofencingservice.dto.*;
import com.example.geofencingservice.grpc.DeviceGrpcClient;
import com.example.geofencingservice.safe_zone.SafeZoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private static final double MAX_SPEED_THRESHOLD = 3.0;
    private final Map<String, PendingSpeedCheck> pendingSpeedChecks = new ConcurrentHashMap<>();
    private final Map<String, Double> latestSpeeds = new ConcurrentHashMap<>();
    private final SafeZoneRepository safeZoneRepository;
    private final DeviceGrpcClient deviceGrpcClient;
    private final RedisService redisService;
    private final RabbitMqService rabbitMqService;


    @KafkaListener(topics = "${app.kafka.topics.gps}")
    public void consume(Envelope event) {
        getChildId(event.deviceId())
                .flatMap(childId -> Mono.when(
                        processLocation(childId, event) ,
                        processSpeed(childId, event),
                        processGps(childId , event.payload())
                ))
                .subscribe();
    }

    public Mono<Void> processGps(String childId, GPS gps) {
        return redisService.publish(childId , gps);
    }


    private Mono<String> getChildId(String deviceId) {
        return redisService.getChild(deviceId)
                .switchIfEmpty(
                        deviceGrpcClient.getChildIdByDeviceId(deviceId)
                                .flatMap(childId ->
                                        redisService.saveChild(deviceId, childId)
                                )
                );
    }

    private Mono<Void> processLocation(String childId, Envelope event) {
        return safeZoneRepository.findZoneNamesContainingLocation(
                        Long.parseLong(childId),
                        event.payload().longitude(),
                        event.payload().latitude()
                )
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(optZone -> {
                    return optZone.map(s -> handleChildInSafeZone(childId, s)).orElseGet(() -> handleChildOutsideSafeZone(childId));
                });
    }

    private Mono<Void> handleChildInSafeZone(String childId, String currentSafeZoneName) {

        return redisService.getLastSafeZone(childId)
                .switchIfEmpty(saveSafeZoneAndPublish(childId, currentSafeZoneName))
                .flatMap(lastSafeZone -> {
                    if (!currentSafeZoneName.equals(lastSafeZone.nameOfLastZone())
                            || !lastSafeZone.isInside()) {

                        return updateSafeZoneAndPublish(childId, currentSafeZoneName);
                    }

                    return Mono.<Void>empty();
                })

                .then();
    }
    private Mono<Void> handleChildOutsideSafeZone(String childId) {
        return redisService.getLastSafeZone(childId)
                .flatMap(lastSafeZone -> {
                    if (lastSafeZone.isInside()) {
                        return clearSafeZoneAndPublish(childId, lastSafeZone.nameOfLastZone());
                    }
                    return Mono.empty();
                });
    }

    private Mono<Void> updateSafeZoneAndPublish(String childId, String safeZoneName) {
        LastSafeZone newSafeZone = LastSafeZone.builder()
                .nameOfLastZone(safeZoneName)
                .isInside(true)
                .build();

        return redisService.saveLastSafeZone(childId, newSafeZone)
                .then(publishSafeZoneEvent(childId, safeZoneName, "ENTERED"));
    }
    private Mono<LastSafeZone> saveSafeZoneAndPublish(String childId, String safeZoneName) {
        LastSafeZone newSafeZone = LastSafeZone.builder()
                .nameOfLastZone(safeZoneName)
                .isInside(true)
                .build();

        return redisService.saveLastSafeZone(childId, newSafeZone)
                .then(publishSafeZoneEvent(childId, safeZoneName, "ENTERED"))
                .thenReturn(newSafeZone);
    }

    private Mono<Void> clearSafeZoneAndPublish(String childId, String previousZoneName) {
        return redisService.updateLastSafeZone(childId)
                .then(publishSafeZoneEvent(childId, previousZoneName, "EXITED"));
    }

    private Mono<Void> publishSafeZoneEvent(String childId, String safeZoneName, String eventType) {
        log.info("Publishing safe zone event: Child ID: {}, Safe Zone Name: {}, Event Type: {}", childId, safeZoneName, eventType);
        SafeZoneEvent event = SafeZoneEvent.builder()
                .childId(Long.parseLong(childId))
                .eventType(eventType)
                .safezoneName(safeZoneName)
                .build();

        return rabbitMqService.sendNotification(event);

    }

    private Mono<Void> processSpeed(String childId, Envelope event) {
        double speed = event.payload().speed();
        latestSpeeds.put(childId, speed);

        if (speed >= MAX_SPEED_THRESHOLD) {
            pendingSpeedChecks.computeIfAbsent(childId, id -> {
                PendingSpeedCheck pending = new PendingSpeedCheck(speed, event.timestamp());
                scheduleAverageSpeedCheck(childId, pending);
                return pending;
            });
        }

        return Mono.empty();
    }

    private void scheduleAverageSpeedCheck(String childId, PendingSpeedCheck pending) {
        Mono.delay(Duration.ofSeconds(25))
                .flatMap(tick -> {
                    pendingSpeedChecks.remove(childId);
                    Double latestSpeed = latestSpeeds.get(childId);
                    if (latestSpeed == null) {
                        return Mono.empty();
                    }
                    double averageSpeed = (pending.firstSpeed() + latestSpeed) / 2.0;
                    if (averageSpeed >= MAX_SPEED_THRESHOLD) {
                        return publishSpeedAlert(childId, averageSpeed);
                    }
                    return Mono.empty();
                })
                .doOnError(error -> log.error("Error during average speed check for child {}", childId, error))
                .subscribe();
    }

    private Mono<Void> publishSpeedAlert(String childId, double speed) {
        SpeedEvent event = SpeedEvent.builder()
                .speed(speed)
                .childId(Long.parseLong(childId))
                .build();
        return rabbitMqService.sendSpeedAlert(event);
    }

    private Mono<Void> emptyMono(){
        return Mono.empty();
    }

    private record PendingSpeedCheck(double firstSpeed, Instant timestamp) {}
}