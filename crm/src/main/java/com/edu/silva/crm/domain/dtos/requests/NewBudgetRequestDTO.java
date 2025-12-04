package com.edu.silva.crm.domain.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record NewBudgetRequestDTO(
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank String terms,
        String observations
) {
}
