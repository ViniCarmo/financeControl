package com.vinicius.finance_api.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    //error 409
    public EmailAlreadyExistsException() {
        super("Email already exists");
    }

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
