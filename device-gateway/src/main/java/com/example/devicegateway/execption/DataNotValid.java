package com.example.devicegateway.execption;

public class DataNotValid extends RuntimeException {
    public DataNotValid() {
        super("the child is not found or the parent is coparent");
    }
}
