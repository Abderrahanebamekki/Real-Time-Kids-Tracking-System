package com.example.ingestionservice.domain;

import lombok.NonNull;

public record GpsEvent(
        @NonNull Double longitude,
        @NonNull Double latitude,
        @NonNull Double speed
) {
}
