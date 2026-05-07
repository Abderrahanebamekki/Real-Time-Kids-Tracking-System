package com.example.notificationservice.service;


import com.example.notificationservice.dto.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final RedisService redisService;

    public Flux<NotificationEvent> getNotifications(String userId){
        return redisService.getNotificationForParent(userId)
                .flatMap(redisService::getNotification);
    }

}
