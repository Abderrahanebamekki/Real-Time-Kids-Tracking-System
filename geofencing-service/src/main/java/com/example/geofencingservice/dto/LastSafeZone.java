package com.example.geofencingservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record LastSafeZone(
        @NotNull String nameOfLastZone,
        @NotNull Boolean isInside
) {
}
