package com.example.geofencingservice.service;


import com.example.geofencingservice.dto.SafeZoneEvent;
import com.example.geofencingservice.dto.SpeedEvent;
import com.example.geofencingservice.rabbitmq.RabbitMqConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class RabbitMqService {

    private final RabbitTemplate rabbitTemplate;

    public Mono<Void> sendSpeedAlert(SpeedEvent speedEvent) {
        return Mono.fromRunnable(() ->
                rabbitTemplate.convertAndSend(
                        RabbitMqConfig.SPEED_EXCHANGE,
                        RabbitMqConfig.SPEED_ROUTING_KEY,
                        speedEvent
                )
        ).then();
    }
    public Mono<Void> sendNotification(SafeZoneEvent safeZoneEvent) {
        return Mono.fromRunnable(() ->
                rabbitTemplate.convertAndSend(
                        RabbitMqConfig.SAFEZONE_EXCHANGE,
                        RabbitMqConfig.SAFEZONE_ROUTING_KEY,
                        safeZoneEvent
                )
        ).then();
    }

}
