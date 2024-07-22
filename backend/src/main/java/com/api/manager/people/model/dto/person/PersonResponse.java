package com.api.manager.people.model.dto.person;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PersonResponse(
        Long id,
        String name,
        String email,
        String cpf,
        LocalDate birthDate
) {
}
