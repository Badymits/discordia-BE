package com.example.discordia.exception;

public class EmailNotFound extends RuntimeException {
    public EmailNotFound(String message) {
        super(message);
    }
}
