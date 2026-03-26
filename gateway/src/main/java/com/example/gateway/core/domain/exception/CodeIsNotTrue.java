package com.example.gateway.core.domain.exception;

public class CodeIsNotTrue extends RuntimeException {
    public CodeIsNotTrue() {
        super("the code is incorrect");
    }
}
