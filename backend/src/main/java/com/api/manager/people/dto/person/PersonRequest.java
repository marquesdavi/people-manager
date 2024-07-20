package com.api.manager.people.dto.person;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PersonRequest(
        String name,
        String email,
        String cpf,
        LocalDate birthDate
) {
}
