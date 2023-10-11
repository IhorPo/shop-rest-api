package com.shoprestapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Illegal email")
public class IllegalEmailException extends RuntimeException{
    public IllegalEmailException(String message){
        super(message);
    }
}
