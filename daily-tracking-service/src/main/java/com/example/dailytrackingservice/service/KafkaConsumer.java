package com.example.dailytrackingservice.service;

import com.example.dailytrackingservice.dto.Envelope;
import com.example.dailytrackingservice.dto.VitalEvent;
import com.example.dailytrackingservice.entities.Vitals;
import com.example.dailytrackingservice.repositories.VitalsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final RedisService redisService;
    private final VitalsRepository vitalsRepository;

    @KafkaListener(topics = "${app.kafka.topic.vitals}")
    public void consumeVitals(Envelope<VitalEvent> envelope) {
        VitalEvent vitalEvent = envelope.payload();
        String childId = envelope.deviceId();

        Vitals vitals = Vitals.builder()
                .childId(childId)
                .heartbeats(vitalEvent.heartbeats())
                .oxygenLevel(vitalEvent.oxygenLevel())
                .timestamp(envelope.timestamp())
                .build();

        vitalsRepository.save(vitals)
                .then(redisService.publishVitals(childId, vitalEvent))
                .then(redisService.saveVitalsToRedis(childId, vitalEvent))
                .then(Mono.fromRunnable(() -> redisService.subscribeToVitalsChannel(childId)))
                .doOnSuccess(unused -> log.info("Processed vitals for child: {}", childId))
                .doOnError(error -> log.error("Error processing vitals for child {}: {}", childId, error.getMessage()))
                .subscribe();
    }
}
