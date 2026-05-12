package com.example.geofencingservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;

public record GPS(
        Double longitude,
        Double latitude,
        Double speed
) {
}
