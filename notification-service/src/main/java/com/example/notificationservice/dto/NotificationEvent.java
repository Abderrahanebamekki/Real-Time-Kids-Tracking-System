package com.example.notificationservice.dto;

import lombok.Builder;

import java.time.LocalDateTime;


@Builder
public record NotificationEvent(
        String message,
        LocalDateTime dateTime,
        MessageType type
) {

}
