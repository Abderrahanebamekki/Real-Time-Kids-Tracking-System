package com.example.gateway.core.domain.globalservice;

public class VerificationCodeGenerator {

    public static String generateCode() {
        int code = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(code);
    }
}
