package com.kropsz.github.backendboxboxd.exception;

public class BusinessViolationException extends RuntimeException{
    public BusinessViolationException(String message){
        super(message);
    }
}
