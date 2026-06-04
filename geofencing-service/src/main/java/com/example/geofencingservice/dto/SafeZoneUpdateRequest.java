package com.example.geofencingservice.dto;

import jakarta.validation.constraints.NotNull;

public record SafeZoneUpdateRequest(
        @NotNull Double radius,
        @NotNull Double longitude,
        @NotNull Double latitude
) {
}
