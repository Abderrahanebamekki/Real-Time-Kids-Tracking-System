package com.example.identityfamily.core.domain.exception;

public class UserNotFound extends RuntimeException {
    public UserNotFound() {
        super("User Not Found");
    }
}
