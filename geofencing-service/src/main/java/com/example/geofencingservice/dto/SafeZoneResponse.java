package com.example.geofencingservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;


@Builder
public record SafeZoneResponse(
        @NotNull Long id,
        @NotNull String name,
        @NotNull Double radius,
        @NotNull Double longitude,
        @NotNull Double latitude
) {
}
