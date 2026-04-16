package com.example.ingestionservice.exception;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(TopicNotSupported.class)
    public void topicNotSupported(TopicNotSupported ex){
        System.out.println(ex.getMessage());
    }

}
