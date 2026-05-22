package com.example.notificationservice.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMqConfig {

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public Queue heartbeatQueue() {
        return new Queue("heartbeat-queue");
    }

    @Bean
    public Queue oxygenQueue() {
        return new Queue("oxygen-queue");
    }

    @Bean
    public Queue batteryQueue() {
        return new Queue("battery-queue");
    }

}
