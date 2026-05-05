package com.example.notificationservice.service;

import com.example.notificationservice.dto.SafeZoneEvent;
import com.example.notificationservice.dto.SpeedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitMqConsumerService {

    private final grpcClient grpcClient;
    private final RedisService redisService;

    @RabbitListener(queues = "${app.rabbitmq.queues.speed}")
    public void consumeSpeedAlert(SpeedEvent event) {
//        grpcClient.getChildName(event.childId())
//                .flatMap(name -> redisService.saveSpeedMessage(event.childId(), "Speed alert for child : "+ name
//                        +" "+ event.speed() + " km/h"))
//                .subscribe();
    }

    @RabbitListener(queues = "${app.rabbitmq.queues.safezone}")
    public void consumeSafeZoneAlert(SafeZoneEvent event) {

//        grpcClient.getChildName(event.childId())
//                .flatMap(name -> redisService.saveSafeZoneMessage(event.childId(), "The child : "+ name
//                        +" was "+ event.eventType() + " " + event.safezoneName()))
//                .subscribe();
    }
}
