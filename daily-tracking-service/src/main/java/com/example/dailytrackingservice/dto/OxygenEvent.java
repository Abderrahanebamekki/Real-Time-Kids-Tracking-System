package com.example.dailytrackingservice.dto;

import lombok.NonNull;

public record OxygenEvent(
        @NonNull Integer oxygenLevel,
        @NonNull String childId
) {
}
