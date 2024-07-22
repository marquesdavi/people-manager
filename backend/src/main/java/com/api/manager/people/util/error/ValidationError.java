package com.api.manager.people.util.error;

import lombok.Data;

@Data
public class ValidationError {

    private String field;
    private String message;
    private Object invalidValue;

    public ValidationError(String field, String message, Object invalidValue) {
        this.field = field;
        this.message = message;
        this.invalidValue = invalidValue;
    }
}

