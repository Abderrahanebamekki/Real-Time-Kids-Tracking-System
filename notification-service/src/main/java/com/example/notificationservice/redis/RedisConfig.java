package com.example.notificationservice.redis;

import com.example.notificationservice.dto.NotificationEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisMessageListenerContainer listenerContainer(
            ReactiveRedisConnectionFactory factory) {
        return new ReactiveRedisMessageListenerContainer(factory);
    }

    @Bean
    public ReactiveRedisTemplate<String, NotificationEvent> reactiveRedisTemplate(
            ReactiveRedisConnectionFactory factory
    ) {

        RedisSerializationContext.RedisSerializationContextBuilder<String, NotificationEvent> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        JacksonJsonRedisSerializer<NotificationEvent> valueSerializer =
                new JacksonJsonRedisSerializer<>(NotificationEvent.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        RedisSerializationContext<String, NotificationEvent> context = builder
                .value(valueSerializer)
                .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}
