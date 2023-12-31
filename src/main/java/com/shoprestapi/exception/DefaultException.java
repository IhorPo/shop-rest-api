package com.shoprestapi.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class DefaultException {
    private final String message;
    private final HttpStatus httpStatus;
    private final LocalDateTime dateTime;

    public DefaultException(String message, HttpStatus httpStatus, LocalDateTime dateTime) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
