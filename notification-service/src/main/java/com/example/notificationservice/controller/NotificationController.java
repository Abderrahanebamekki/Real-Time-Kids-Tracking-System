package com.example.notificationservice.controller;

import com.example.notificationservice.dto.NotificationEvent;
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

    private final RedisService redisService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<NotificationEvent> subscribe(
            @RequestHeader("X-User-Id") String parentId
    ) {
        return redisService.subscribe(parentId);
    }
}
