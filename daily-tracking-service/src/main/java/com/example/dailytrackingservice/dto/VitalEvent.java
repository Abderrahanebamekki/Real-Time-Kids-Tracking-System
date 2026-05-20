package com.example.dailytrackingservice.dto;

import lombok.NonNull;

public record VitalEvent(
        @NonNull Integer heartbeats,
        @NonNull Integer oxygenLevel
) {
}
