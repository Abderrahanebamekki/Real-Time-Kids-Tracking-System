package com.example.ingestionservice.service;

import com.example.ingestionservice.exception.TopicNotSupported;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static reactor.netty.http.HttpConnectionLiveness.log;

@Service
public class IngestionRoutingService {
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, JsonNode> kafkaTemplate;


    public  IngestionRoutingService(KafkaTemplate<String, JsonNode> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
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
        double latitude = node.get("heartbeats").asDouble();
        double longitude = node.get("oxygenLevel").asDouble();

        log.info("📍 GPS received from device [{}]: lat={}, lon={}, speed={}, temp={}",
                deviceId, latitude, longitude);
        return Mono.empty();
    }

    private Mono<Void> publishGps(String deviceId, JsonNode node) {
        assert verifyGpsData(node);

        return Mono.fromFuture(
                        kafkaTemplate.send("gps", deviceId, node)
                                .toCompletableFuture()
                )
                .doOnSuccess(v -> System.out.println("Sent GPS for " + deviceId))
                .doOnError(e -> System.err.println("Kafka error: " + e.getMessage()))
                .then();
    }

    private Boolean verifyGpsData(JsonNode node) {
        return true;
    }

}