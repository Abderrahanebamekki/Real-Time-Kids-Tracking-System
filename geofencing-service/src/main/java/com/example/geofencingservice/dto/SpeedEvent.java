package com.example.geofencingservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SpeedEvent(
        @NotNull Long childId,
        @NotNull Double speed
) {
}
