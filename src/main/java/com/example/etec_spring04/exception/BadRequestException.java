package com.example.etec_spring04.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);//extend message from RuntimeException, in order to throw exception easily
    }
}
