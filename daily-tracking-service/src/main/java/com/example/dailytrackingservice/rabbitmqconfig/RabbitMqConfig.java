package com.example.dailytrackingservice.rabbitmqconfig;

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

    public static final String EXCHANGE_V = "vitals";
    public static final String EXCHANGE_B = "device";
    public static final String ROUTING_HEARTBEAT = "vitals.heartbeat";
    public static final String ROUTING_OXYGEN = "vitals.oxygen";
    public static final String ROUTING_BATTERY = "device.battery";

    @Bean
    public DirectExchange vitalsExchange() {
        return new DirectExchange(EXCHANGE_V);
    }

    @Bean
    public DirectExchange batteryExchange() {
        return new DirectExchange(EXCHANGE_B);
    }

    @Bean
    public Queue heartbeatQueue() {
        return new Queue("heartbeat.queue");
    }

    @Bean
    public Queue oxygenQueue() {
        return new Queue("oxygen.queue");
    }

    @Bean
    public Binding heartbeatBinding() {
        return BindingBuilder.bind(heartbeatQueue())
                .to(vitalsExchange())
                .with(ROUTING_HEARTBEAT);
    }

    @Bean
    public Binding oxygenBinding() {
        return BindingBuilder.bind(oxygenQueue())
                .to(vitalsExchange())
                .with(ROUTING_OXYGEN);
    }

    @Bean
    public Queue batteryQueue() {
        return new Queue("battery.queue");
    }

    @Bean
    public Binding batteryBinding() {
        return BindingBuilder.bind(batteryQueue())
                .to(batteryExchange())
                .with(ROUTING_BATTERY);
    }
    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
