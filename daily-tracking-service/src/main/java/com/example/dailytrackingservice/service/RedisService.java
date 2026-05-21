package com.example.dailytrackingservice.service;

import com.example.dailytrackingservice.dto.Envelope;
import com.example.dailytrackingservice.dto.VitalEvent;
import com.example.dailytrackingservice.entities.Vitals;
import com.example.dailytrackingservice.repositories.VitalsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {

    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;
    private final VitalsRepository vitalsRepository;
    private final ObjectMapper objectMapper;

    public Mono<String> getChildId(String deviceId) {
        return reactiveRedisTemplate.opsForValue().get(deviceId);
    }

    public Mono<Void> publishVitals(Envelope<VitalEvent> envelope) {
        String channel = buildChannel(envelope.deviceId());
        return toJson(envelope.payload())
                .flatMap(json -> reactiveRedisTemplate.convertAndSend(channel, json))
                .then();
    }

    public Mono<Void> saveVitalsToRedis(Envelope<VitalEvent> envelope) {
        String key = buildChannel(envelope.deviceId());
        return toJson(envelope.payload())
                .flatMap(json -> reactiveRedisTemplate.opsForValue().set(key, json))
                .then();
    }

    @PostConstruct
    public void subscribeToVitalsChannel() {
        ReactiveRedisMessageListenerContainer container =
                new ReactiveRedisMessageListenerContainer(reactiveRedisTemplate.getConnectionFactory());

        container.receive(ChannelTopic.of("ch:*:v"))
                .flatMap(message -> {
                    try {
                        String childId = extractChildId(message.getChannel());
                        VitalEvent vitalEvent = objectMapper.readValue(message.getMessage(), VitalEvent.class);
                        Vitals vitals = Vitals.builder()
                                .childId(childId)
                                .heartbeats(vitalEvent.heartbeats())
                                .oxygenLevel(vitalEvent.oxygenLevel())
                                .timestamp(java.time.Instant.now())
                                .build();
                        return vitalsRepository.save(vitals).then();
                    } catch (Exception e) {
                        log.error("Error processing vitals message from Redis pub/sub", e);
                        return Mono.empty();
                    }
                })
                .subscribe();
    }

    private Mono<String> toJson(VitalEvent event) {
        try {
            return Mono.just(objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }

    private String buildChannel(String deviceId) {
        return "ch:" + deviceId + ":v";
    }

    private String extractChildId(String channel) {
        return channel.replace("ch:", "").replace(":v", "");
    }
}
