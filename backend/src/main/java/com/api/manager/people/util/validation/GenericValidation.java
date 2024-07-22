package com.api.manager.people.util.validation;

import org.springframework.data.domain.Sort;

import java.util.Arrays;

import static com.api.manager.people.util.validation.DocumentValidation.isValidCpf;

public class GenericValidation {

    public static void validateOrderBy(Class<?> objectType, String orderBy) {
        boolean fieldMatch = Arrays.stream(objectType.getDeclaredFields())
                .noneMatch(item -> item.getName().equals(orderBy));

        if (fieldMatch) {
            throw new IllegalArgumentException("The field " + orderBy + " doesn't exist in " + objectType.getSimpleName());
        }
    }

    public static Sort.Direction validateSortDirection(String direction) {
        try {
            return Sort.Direction.valueOf(direction.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid direction: " + direction + ". Valid values are 'ASC' or 'DESC'.");
        }
    }

    public static void validateCpf(String cpf) {
        if(!isValidCpf(cpf)){
            throw new IllegalArgumentException("Invalid CPF!");
        }
    }
}
