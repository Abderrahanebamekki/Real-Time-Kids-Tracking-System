package com.example.geofencingservice.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String SPEED_EXCHANGE = "speed.exchange";
    public static final String SAFEZONE_EXCHANGE = "safezone.exchange";

    public static final String SPEED_QUEUE = "speed.queue";
    public static final String SAFEZONE_QUEUE = "safezone.queue";

    public static final String SPEED_ROUTING_KEY = "speed.alert";
    public static final String SAFEZONE_ROUTING_KEY = "safezone.notification";

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public DirectExchange speedExchange() {
        return new DirectExchange(SPEED_EXCHANGE);
    }

    @Bean
    public DirectExchange safeZoneExchange() {
        return new DirectExchange(SAFEZONE_EXCHANGE);
    }

    @Bean
    public Queue speedQueue() {
        return new Queue(SPEED_QUEUE, true);
    }

    @Bean
    public Queue safeZoneQueue() {
        return new Queue(SAFEZONE_QUEUE, true);
    }

    @Bean
    public Binding speedBinding() {
        return BindingBuilder
                .bind(speedQueue())
                .to(speedExchange())
                .with(SPEED_ROUTING_KEY);
    }

    @Bean
    public Binding safeZoneBinding() {
        return BindingBuilder
                .bind(safeZoneQueue())
                .to(safeZoneExchange())
                .with(SAFEZONE_ROUTING_KEY);
    }
}
