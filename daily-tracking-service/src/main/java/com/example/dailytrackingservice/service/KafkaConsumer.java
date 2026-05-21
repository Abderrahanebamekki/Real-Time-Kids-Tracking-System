package com.example.dailytrackingservice.service;

import com.example.dailytrackingservice.dto.Envelope;
import com.example.dailytrackingservice.dto.HeartbeatEvent;
import com.example.dailytrackingservice.dto.OxygenEvent;
import com.example.dailytrackingservice.dto.VitalEvent;
import com.example.dailytrackingservice.entities.Vitals;
import com.example.dailytrackingservice.repositories.VitalsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private static final int HEARTBEAT_MIN = 60;
    private static final int HEARTBEAT_MAX = 100;
    private static final int OXYGEN_MIN = 90;

    private final RedisService redisService;
    private final VitalsRepository vitalsRepository;
    private final RabbitMqProducerService rabbitMqProducerService;

    @KafkaListener(topics = "${app.kafka.topic.vitals}")
    public void consumeVitals(Envelope<VitalEvent> envelope) {
        VitalEvent vitalEvent = envelope.payload();
        String childId = envelope.deviceId();

        checkAbnormalHeartbeat(vitalEvent.heartbeats(), childId);
        checkAbnormalOxygen(vitalEvent.oxygenLevel(), childId);

        Vitals vitals = Vitals.builder()
                .childId(childId)
                .heartbeats(vitalEvent.heartbeats())
                .oxygenLevel(vitalEvent.oxygenLevel())
                .timestamp(envelope.timestamp())
                .build();

        vitalsRepository.save(vitals)
                .then(redisService.publishVitals(childId, vitalEvent))
                .then(redisService.saveVitalsToRedis(childId, vitalEvent))
                .subscribe();
    }

    private void checkAbnormalHeartbeat(int heartbeats, String childId) {
        if (heartbeats < HEARTBEAT_MIN || heartbeats > HEARTBEAT_MAX) {
            rabbitMqProducerService.sendAbnormalHeartbeat(new HeartbeatEvent(heartbeats, childId));
        }
    }

    private void checkAbnormalOxygen(int oxygenLevel, String childId) {
        if (oxygenLevel < OXYGEN_MIN) {
            rabbitMqProducerService.sendAbnormalOxygen(new OxygenEvent(oxygenLevel, childId));
        }
    }
}
