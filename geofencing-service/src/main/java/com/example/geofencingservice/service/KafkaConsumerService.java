package com.example.geofencingservice.service;

import com.example.geofencingservice.dto.Envelope;
import com.example.geofencingservice.safe_zone.SafeZoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final SafeZoneRepository  safeZoneRepository;

    @KafkaListener(topics = "${app.kafka.topics.gps}")
    public void consume(Envelope<JsonNode> event) {
        event.
        System.out.println(event);
    }
}
