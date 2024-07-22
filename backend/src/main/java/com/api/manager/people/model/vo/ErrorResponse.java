package com.api.manager.people.model.vo;

import com.api.manager.people.util.error.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.List;


public record ErrorResponse(String message, HttpStatusCode statusCode, List<ValidationError> errors) {
    public ErrorResponse(String message, HttpStatusCode statusCode) {
        this(message, statusCode, null);
    }
}

