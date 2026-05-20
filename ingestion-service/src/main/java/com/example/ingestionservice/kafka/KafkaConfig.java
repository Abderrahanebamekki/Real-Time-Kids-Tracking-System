package com.example.ingestionservice.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic GPSTopic() {
        return TopicBuilder.name("gps")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic vitalsTopic() {
        return TopicBuilder.name("vitals")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
