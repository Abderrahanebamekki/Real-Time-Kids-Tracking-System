package com.example.ingestionservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record Envelope<T>(
        @NotNull UUID eventId,
        @NotBlank String deviceId,
        @NotNull DeviceEventType type,
        @NotNull Instant timestamp,
        @NotNull T payload
) {}
