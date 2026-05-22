package com.example.notificationservice.dto;

public record HeartbeatEvent(
        Integer heartbeats,
        String childId
) {
}
