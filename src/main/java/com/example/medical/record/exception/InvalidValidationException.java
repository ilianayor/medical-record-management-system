package com.example.medical.record.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidValidationException extends RuntimeException {
    public InvalidValidationException(String message) {
        super(message);
    }

    public InvalidValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
