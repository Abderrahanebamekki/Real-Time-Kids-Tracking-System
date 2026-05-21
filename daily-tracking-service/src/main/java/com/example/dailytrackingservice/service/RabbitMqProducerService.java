package com.example.dailytrackingservice.service;

import com.example.dailytrackingservice.dto.BatteryEvent;
import com.example.dailytrackingservice.dto.HeartbeatEvent;
import com.example.dailytrackingservice.dto.OxygenEvent;
import com.example.dailytrackingservice.rabbitmqconfig.RabbitMqConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMqProducerService {

    private final RabbitTemplate rabbitTemplate;

    public void sendAbnormalHeartbeat(HeartbeatEvent event) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, RabbitMqConfig.ROUTING_HEARTBEAT, event);
        log.info("Sent abnormal heartbeat event: {}", event);
    }

    public void sendAbnormalOxygen(OxygenEvent event) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, RabbitMqConfig.ROUTING_OXYGEN, event);
        log.info("Sent abnormal oxygen event: {}", event);
    }

    public void sendAbnormalBattery(BatteryEvent event) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, RabbitMqConfig.ROUTING_BATTERY, event);
        log.info("Sent abnormal battery event: {}", event);
    }
}
