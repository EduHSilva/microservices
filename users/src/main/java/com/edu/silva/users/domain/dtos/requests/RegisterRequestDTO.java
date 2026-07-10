package com.edu.silva.users.domain.dtos.requests;

import com.edu.silva.users.domain.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;
public record RegisterRequestDTO(
        @NotBlank @Email String email,
        @NotBlank String username,
        @NotBlank String password,
        @NotNull UserRole role,
        UUID companyID
) {
}
