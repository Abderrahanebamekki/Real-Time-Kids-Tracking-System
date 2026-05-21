package com.example.dailytrackingservice.dto;

import lombok.NonNull;

public record BatteryEvent(
        @NonNull String batteryLevel,
        @NonNull String childId
) {
}
