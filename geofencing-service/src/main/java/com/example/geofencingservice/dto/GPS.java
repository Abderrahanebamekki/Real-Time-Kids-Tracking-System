package com.example.geofencingservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;

public record GPS(
        @NonNull Double longitude,
        @NonNull Double latitude,
        @NonNull Double speed
) {
}
