package com.demo.demo.exception;


import com.demo.demo.exception.exceptions.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyExceptionHandler {

    // muc tieu : bat loi va return message cho phia FE
    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ResponseEntity handleBadRequestException(MethodArgumentNotValidException exception){
        System.out.println("User not yet input");
        String responseMessage ="";
        for(FieldError fileError: exception.getFieldErrors()){
            responseMessage += fileError.getDefaultMessage()+ "\n";
        }
        return new ResponseEntity(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity handleAuthenticationException(AuthenticationException exception){
        return new ResponseEntity(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}


// ORM
// Database
