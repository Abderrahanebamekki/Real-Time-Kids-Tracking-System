package com.example.ingestionservice.domain;

import lombok.NonNull;

public record BatteryEvent(
        @NonNull String level
) {
}
