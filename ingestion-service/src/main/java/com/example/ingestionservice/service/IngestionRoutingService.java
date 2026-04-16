package com.example.ingestionservice.service;

import com.example.ingestionservice.exception.TopicNotSupported;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class IngestionRoutingService {
    private final ObjectMapper objectMapper;

    public  IngestionRoutingService() {
        this.objectMapper = new ObjectMapper();
    }

    public Mono<Void> route(String topic, String rawPayload) throws Exception{

            String[] parts = topic.split("/");
            String deviceId = parts[1];
            String metric = parts[2];

            JsonNode node = objectMapper.readTree(rawPayload);

            return switch (metric) {
                case "gps" -> publishGps(deviceId, node);
                case "vitals" -> publishVitals(deviceId, node);
                default -> throw new TopicNotSupported(metric);
            };

    }

    private Mono<Void> publishVitals(String deviceId, JsonNode node) {
        return Mono.empty();
    }

    private Mono<Void> publishGps(String deviceId, JsonNode node) {
        return Mono.empty();
    }


}