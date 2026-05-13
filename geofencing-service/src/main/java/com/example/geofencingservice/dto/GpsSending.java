package com.example.geofencingservice.dto;

import java.time.Instant;

public record GpsSending(
        Double longitude,
        Double latitude,
        Double speed,
        Instant timestamp
) {
}
