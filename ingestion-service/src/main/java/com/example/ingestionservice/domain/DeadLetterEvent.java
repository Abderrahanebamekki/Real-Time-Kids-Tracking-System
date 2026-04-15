package com.example.ingestionservice.domain;

import java.time.Instant;

public record DeadLetterEvent(
        String originalTopic,
        String payload,
        String reason,
        Instant failedAt
) {}
