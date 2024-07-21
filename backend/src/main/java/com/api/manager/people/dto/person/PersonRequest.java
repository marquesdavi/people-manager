package com.api.manager.people.dto.person;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
public record PersonRequest(
        @NotEmpty(message = "Name field can't be empty")
        String name,
        @Email(message = "Email field must follow the 'name@provider.domain' pattern")
        String email,
        @NotEmpty(message = "CPF field can't be empty")
        String cpf,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate birthDate
) {
}
