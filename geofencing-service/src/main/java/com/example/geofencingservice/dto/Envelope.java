package com.example.geofencingservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record Envelope(
        @NotNull UUID eventId,
        @NotBlank String deviceId,
        @NotNull Instant timestamp,
        @NotNull GPS payload
) {}
