package com.example.identityfamily.core.domain.exception;


public class EmailAlreadyExists extends RuntimeException {

    public EmailAlreadyExists(String username) {
        super(String.format("Email '%s' already exists!", username));
    }

}
