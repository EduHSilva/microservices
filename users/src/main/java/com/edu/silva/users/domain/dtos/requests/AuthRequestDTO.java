package com.edu.silva.users.domain.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequestDTO(@NotBlank @Email String email, @NotBlank String password) {
}
