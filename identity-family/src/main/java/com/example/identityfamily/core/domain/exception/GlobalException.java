package com.example.identityfamily.core.domain.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {



    @ExceptionHandler(PhoneNumberNotValid.class)
    public ResponseEntity<String> phoneNumberNotValid(PhoneNumberNotValid ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(PhoneNumberAlreadyExist.class)
    public ResponseEntity<String> phoneNumberAlreadyExist(PhoneNumberAlreadyExist ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
