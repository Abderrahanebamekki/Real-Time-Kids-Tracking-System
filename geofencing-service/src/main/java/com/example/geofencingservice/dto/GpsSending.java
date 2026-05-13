package com.example.geofencingservice.dto;

import lombok.Builder;

import java.time.Instant;


@Builder
public record GpsSending(
        Double longitude,
        Double latitude,
        Double speed,
        Instant timestamp
) {
}
