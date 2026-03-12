package com.edu.silva.users.domain.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record UpdateCompanyRequestDTO(@NotBlank String name) {
}
