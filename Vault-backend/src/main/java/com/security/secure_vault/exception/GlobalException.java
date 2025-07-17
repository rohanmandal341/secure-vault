package com.security.secure_vault.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIO(IOException ex){
        return new ResponseEntity<>("error : " + ex.getMessage(),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> genericException(Exception ex){
        return new ResponseEntity<>("error : " + ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
