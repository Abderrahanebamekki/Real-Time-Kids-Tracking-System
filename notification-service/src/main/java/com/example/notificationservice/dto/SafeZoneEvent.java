package com.example.notificationservice.dto;

public record SafeZoneEvent(
        Long childId,
        String safeZoneName,
        String eventType
) {
}
