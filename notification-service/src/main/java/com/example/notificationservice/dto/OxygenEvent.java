package com.example.notificationservice.dto;

public record OxygenEvent(
        Integer oxygenLevel,
        String childId
) {
}
