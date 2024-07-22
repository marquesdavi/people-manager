package com.api.manager.people.model.dto.user;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotNull
        @NotBlank
        @Size(min = 8, max = 30)
        @Email
        String email,
        @NotNull
        @NotBlank
        @Size(min = 8, max = 50)
        String password,
        String firstName,
        String lastName
) {
}
