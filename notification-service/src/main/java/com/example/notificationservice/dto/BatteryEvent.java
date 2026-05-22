package com.example.notificationservice.dto;

public record BatteryEvent(
        String batteryLevel,
        String childId
) {
}
