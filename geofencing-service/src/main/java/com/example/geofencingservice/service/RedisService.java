package com.example.geofencingservice.service;


import com.example.geofencingservice.dto.GPS;
import com.example.geofencingservice.dto.GpsSending;
import com.example.geofencingservice.dto.LastSafeZone;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@Service
@RequiredArgsConstructor
public class RedisService {

    private final ReactiveStringRedisTemplate stringRedisTemplate;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public Mono<String> saveChild(String deviceId, Long childId) {
        return stringRedisTemplate.opsForValue()
                .set(deviceId, childId.toString())
                .thenReturn(deviceId);
    }

    public Mono<String> getChild(String deviceId) {
        return stringRedisTemplate.opsForValue()
                .get(deviceId);
    }

    public Mono<Void> saveLastSafeZone(String childId, LastSafeZone lastSafeZone) {
        return Mono.fromCallable(() -> OBJECT_MAPPER.writeValueAsString(lastSafeZone))
                .flatMap(json -> stringRedisTemplate.opsForValue().set(childId, json))
                .then()
                .onErrorResume(JsonProcessingException.class, e ->
                        Mono.error(new RuntimeException("Failed to serialize LastSafeZone", e))
                );
    }

    public Mono<LastSafeZone> getLastSafeZone(String childId) {
        return stringRedisTemplate.opsForValue()
                .get(childId)
                .flatMap(json -> Mono.fromCallable(() ->
                        OBJECT_MAPPER.readValue(json, LastSafeZone.class)
                ))
                .onErrorResume(JsonProcessingException.class, e ->
                        Mono.error(new RuntimeException("Failed to deserialize LastSafeZone", e))
                );
    }


    public Mono<Void> updateLastSafeZone(String childId) {
        return stringRedisTemplate.opsForValue()
                .get(childId)
                .flatMap(json -> Mono.fromCallable(() -> OBJECT_MAPPER.readValue(json, LastSafeZone.class)))
                .flatMap(safeZone -> {
                    LastSafeZone updated = new LastSafeZone(
                            safeZone.nameOfLastZone(),
                            false
                    );
                    return Mono.fromCallable(() -> OBJECT_MAPPER.writeValueAsString(updated))
                            .flatMap(updatedJson -> stringRedisTemplate.opsForValue()
                                    .set(childId, updatedJson));
                })
                .then();
    }

    public Mono<Void> publish(String childId, GpsSending gps) {
        String channel = "child:" + childId + ":gps";

        return Mono.fromCallable(() -> OBJECT_MAPPER.writeValueAsString(gps))
                .flatMap(json -> stringRedisTemplate.convertAndSend(channel, json))
                .then();
    }

    public Flux<GpsSending> subscribe(String childId) {

        String channel = "child:" + childId + ":gps";

        return stringRedisTemplate
                .listenTo(ChannelTopic.of(channel))
                .map(ReactiveSubscription.Message::getMessage)
                .flatMap(json ->
                        Mono.fromCallable(() ->
                                OBJECT_MAPPER.readValue(json, GpsSending.class)
                        )
                );
    }

}