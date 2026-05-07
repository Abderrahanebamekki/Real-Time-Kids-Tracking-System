package com.example.notificationservice.controller;

import com.example.notificationservice.dto.NotificationEvent;
import com.example.notificationservice.service.NotificationService;
import com.example.notificationservice.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<NotificationEvent>> getNotifications(
            @RequestHeader("X-User-Id") String userId
    ) {
        Flux<ServerSentEvent<NotificationEvent>> notifications =
                notificationService.getNotifications(userId)
                        .map(message -> ServerSentEvent.<NotificationEvent>builder()
                                .id(String.valueOf(System.currentTimeMillis()))
                                .event("notification")
                                .data(message)
                                .build()
                        );

        Flux<ServerSentEvent<NotificationEvent>> heartbeat =
                Flux.interval(Duration.ofSeconds(15))
                        .map(i -> ServerSentEvent.<NotificationEvent>builder()
                                .comment("keep-alive")
                                .build()
                        );

        return Flux.merge(notifications, heartbeat)
                .doOnSubscribe(s -> System.out.println("Client connected: " + userId))
                .doOnCancel(() -> System.out.println("Client disconnected: " + userId))
                .doOnError(e -> System.out.println("SSE error: " + e.getMessage()));
    }
}
