package com.example.dailytrackingservice.dto;

import lombok.NonNull;

public record HeartbeatEvent(
        @NonNull Integer heartbeats,
        @NonNull String childId
) {
}
