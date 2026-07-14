package com.example.taskservice.exception;

public class UserServiceUnavailableException extends RuntimeException {

    public UserServiceUnavailableException(String message) {
        super(message);
    }

}