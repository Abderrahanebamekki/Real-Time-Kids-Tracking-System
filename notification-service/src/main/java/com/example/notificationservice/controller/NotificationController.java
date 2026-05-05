package com.example.notificationservice.controller;

import com.example.notificationservice.dto.NotificationEvent;
import com.example.notificationservice.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final RedisService redisService;

    @GetMapping(value = "/subscribe/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<NotificationEvent>> getNotifications(
            @RequestHeader("X-User-Id") String userId
    ) {
        return redisService.subscribe(userId)
                .map(message -> ServerSentEvent.<NotificationEvent>builder()
                        .id(String.valueOf(System.currentTimeMillis()))
                        .event("notification")
                        .data(message)
                        .build()
                )
                .doOnCancel(() -> System.out.println("Client disconnected: " + userId));
    }
}
