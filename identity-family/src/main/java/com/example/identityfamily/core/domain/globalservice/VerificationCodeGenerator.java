package com.example.identityfamily.core.domain.globalservice;

import org.springframework.stereotype.Component;

public class VerificationCodeGenerator {

    public static String generateCode() {
        int code = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(code);
    }
}
