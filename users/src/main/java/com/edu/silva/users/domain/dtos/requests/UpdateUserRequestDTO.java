package com.edu.silva.users.domain.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequestDTO(@NotBlank String username) {
}
