package com.example.ingestionservice.mqtt;

import com.example.ingestionservice.service.IngestionRoutingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MqttMessageHandler implements MessageHandler {

    private final IngestionRoutingService routingService;


    @Override
    public void handleMessage(Message<?> message) {
        String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
        String payload = String.valueOf(message.getPayload());
        try {
            assert topic != null;
            if (topic != null && topic.startsWith("$events/")) {
                log.info("EMQX EVENT [{}]: {}", topic, payload);
                return;
            }
            routingService.route(topic, payload).subscribe();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
