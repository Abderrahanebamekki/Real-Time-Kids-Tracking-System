package com.example.ingestionservice.domain;

import lombok.NonNull;

public record GpsEvent(
        @NonNull Double longtitude,
        @NonNull Double latitude,
        @NonNull Double speed
) {
}
