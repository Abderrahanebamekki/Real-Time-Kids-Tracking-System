package com.example.identityfamily.core.domain.exception;

public class CodeExpiredException extends RuntimeException {
    public CodeExpiredException() {
        super("This code has expired. Please try again later.");
    }
}
