package com.example.ingestionservice.exception;

public class TopicNotSupported extends RuntimeException {
    public TopicNotSupported(String message) {
        super("Topics not supported: " + message);
    }
}
