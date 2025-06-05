package com.demo.demo.exception.exceptions;

public class AuthenticationException extends RuntimeException{
    public AuthenticationException(String message){
        super(message);
    }
}
