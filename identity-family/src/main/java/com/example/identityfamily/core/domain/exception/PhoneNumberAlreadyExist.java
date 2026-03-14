package com.example.identityfamily.core.domain.exception;

public class PhoneNumberAlreadyExist extends RuntimeException {
    public PhoneNumberAlreadyExist() {
        super("This phone number already exist.");
    }
}
