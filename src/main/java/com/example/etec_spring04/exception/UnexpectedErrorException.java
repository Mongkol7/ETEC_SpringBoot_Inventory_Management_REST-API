package com.example.etec_spring04.exception;

public class UnexpectedErrorException extends RuntimeException {

    public UnexpectedErrorException(String message) {
        super(message);
    }

    public UnexpectedErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
