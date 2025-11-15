package com.edu.silva.users.domain.dtos.requests;

import com.edu.silva.users.domain.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDTO(
        @NotBlank @Email String email,
        @NotBlank String username,
        @NotBlank String password,
        UserRole role
) {
}
