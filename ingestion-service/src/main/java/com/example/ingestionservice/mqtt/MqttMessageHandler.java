package com.example.ingestionservice.mqtt;

import com.example.ingestionservice.service.IngestionRoutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MqttMessageHandler implements MessageHandler {

    private final IngestionRoutingService routingService;


    @Override
    public void handleMessage(Message<?> message) {
        String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
        String payload = String.valueOf(message.getPayload());
        try {
            assert topic != null;
            routingService.route(topic, payload).subscribe();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
