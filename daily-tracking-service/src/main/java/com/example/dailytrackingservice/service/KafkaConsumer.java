package com.example.dailytrackingservice.service;

import com.example.dailytrackingservice.dto.BatteryEvent;
import com.example.dailytrackingservice.dto.Envelope;
import com.example.dailytrackingservice.dto.HeartbeatEvent;
import com.example.dailytrackingservice.dto.OxygenEvent;
import com.example.dailytrackingservice.dto.VitalEvent;
import com.example.dailytrackingservice.entities.Vitals;
import com.example.dailytrackingservice.repositories.VitalsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private static final int HEARTBEAT_MIN = 60;
    private static final int HEARTBEAT_MAX = 100;
    private static final int OXYGEN_MIN = 90;
    private static final Set<Integer> ABNORMAL_BATTERY_LEVELS = Set.of(20, 15, 10, 5, 2);

    private final RedisService redisService;
    private final VitalsRepository vitalsRepository;
    private final RabbitMqProducerService rabbitMqProducerService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.kafka.topic.vitals}")
    public void consumeVitals(Envelope<?> envelope) {
        VitalEvent vitalEvent = objectMapper.convertValue(envelope.payload(), VitalEvent.class);
        String deviceId = envelope.deviceId();

        redisService.getChildId(deviceId)
                .flatMap(childId -> {
                    checkAbnormalHeartbeat(vitalEvent.heartbeats(), childId);
                    checkAbnormalOxygen(vitalEvent.oxygenLevel(), childId);

                    Vitals vitals = Vitals.builder()
                            .childId(childId)
                            .heartbeats(vitalEvent.heartbeats())
                            .oxygenLevel(vitalEvent.oxygenLevel())
                            .timestamp(envelope.timestamp())
                            .build();

                    return vitalsRepository.save(vitals)
                            .then(redisService.publishVitals(childId, vitalEvent))
                            .then(redisService.saveVitalsToRedis(childId, vitalEvent));
                })
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

    @KafkaListener(topics = "${app.kafka.topic.battery}")
    public void consumeBattery(Envelope<?> envelope) {
        String batteryStr = envelope.payload().toString();
        String childId = redisService.getChildId(envelope.deviceId()).block();

        checkAbnormalBattery(batteryStr, childId);
        redisService.saveBatteryToRedis(childId, batteryStr).subscribe();
    }

    private void checkAbnormalBattery(String batteryStr, String childId) {
        try {
            int batteryLevel = Integer.parseInt(batteryStr);
            if (ABNORMAL_BATTERY_LEVELS.contains(batteryLevel)) {
                rabbitMqProducerService.sendAbnormalBattery(new BatteryEvent(batteryStr, childId));
            }
        } catch (NumberFormatException e) {
            log.warn("Invalid battery level format: {}", batteryStr);
        }
    }
}
