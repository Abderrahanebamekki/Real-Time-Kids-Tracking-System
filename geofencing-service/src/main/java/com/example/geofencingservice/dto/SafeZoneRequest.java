package com.example.geofencingservice.dto;

import com.example.geofencingservice.safe_zone.Point;
import jakarta.validation.constraints.NotNull;

public record SafeZoneRequest(
        @NotNull String name,
        @NotNull Double radius,
        @NotNull Double longitude ,
        @NotNull Double latitude,
        @NotNull Long childId
) {
}
