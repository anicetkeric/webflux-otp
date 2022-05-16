package com.bootteam.springsecuritywebfluxotp.common.exception;

public class ValidatorException extends RuntimeException {

    public ValidatorException() {
    }

    public ValidatorException(String message) {
        super(message);
    }

    public ValidatorException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
