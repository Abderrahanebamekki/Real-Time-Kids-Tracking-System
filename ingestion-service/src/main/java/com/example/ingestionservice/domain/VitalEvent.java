package com.example.ingestionservice.domain;

import lombok.NonNull;

public record VitalEvent(
        @NonNull Integer heartbeats,
        @NonNull Integer oxygenLevel
) {
}
