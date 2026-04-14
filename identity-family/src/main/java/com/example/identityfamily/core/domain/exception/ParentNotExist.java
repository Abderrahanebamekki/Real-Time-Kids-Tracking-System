package com.example.identityfamily.core.domain.exception;

public class ParentNotExist extends RuntimeException {
    public ParentNotExist() {
        super("Parent not exist");
    }
}
