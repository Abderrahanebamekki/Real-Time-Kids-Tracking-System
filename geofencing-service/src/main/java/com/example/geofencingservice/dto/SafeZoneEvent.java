package com.example.geofencingservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SafeZoneEvent(
        @NotNull Long childId,
        @NotNull String safezoneName,
        @NotNull String eventType
) {
}
