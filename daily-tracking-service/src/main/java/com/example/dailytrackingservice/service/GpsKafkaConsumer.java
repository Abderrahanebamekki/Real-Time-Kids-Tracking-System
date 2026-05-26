package com.example.dailytrackingservice.service;

import com.example.dailytrackingservice.dto.Envelope;
import com.example.dailytrackingservice.dto.GPS;
import com.example.dailytrackingservice.entities.GpsLog;
import com.example.dailytrackingservice.repositories.GpsLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GpsKafkaConsumer {

    private final RedisService redisService;
    private final GpsLogRepository gpsLogRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.kafka.topic.gps}")
    public void consume(Envelope<?> envelope) {
        GPS gps = objectMapper.convertValue(envelope.payload(), GPS.class);
        String deviceId = envelope.deviceId();

        redisService.getChildId(deviceId)
                .flatMap(childId -> {
                    GpsLog gpsLog = GpsLog.builder()
                            .childId(childId)
                            .longitude(gps.longitude())
                            .latitude(gps.latitude())
                            .speed(gps.speed())
                            .timestamp(envelope.timestamp())
                            .build();
                    return gpsLogRepository.save(gpsLog);
                })
                .doOnSuccess(saved -> log.debug("Saved GPS log for child {}", saved != null ? saved.getChildId() : "unknown"))
                .doOnError(error -> log.error("Error processing GPS event: {}", error.getMessage()))
                .subscribe();
    }
}
