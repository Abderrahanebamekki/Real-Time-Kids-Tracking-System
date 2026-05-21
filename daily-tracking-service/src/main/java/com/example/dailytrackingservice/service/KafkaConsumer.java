package com.example.dailytrackingservice.service;

import com.example.dailytrackingservice.dto.Envelope;
import com.example.dailytrackingservice.dto.VitalEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final RedisService redisService;

    @KafkaListener(topics = "${app.kafka.topic.vitals}", groupId = "${spring.kafka.consumer.group-id:daily-tracking-group}")
    public void consumeVitals(Envelope<VitalEvent> envelope) {
        log.info("Received vitals event for device: {}", envelope.deviceId());

        redisService.publishVitals(envelope).subscribe();
        redisService.saveVitalsToRedis(envelope).subscribe();
    }
}
