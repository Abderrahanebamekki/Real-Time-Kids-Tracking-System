package com.example.identityfamily.core.domain.exception;


public class PhoneNumberNotValid extends RuntimeException {

    public PhoneNumberNotValid() {
        super("phone number must be 10 digits");
    }
}
