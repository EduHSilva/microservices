package com.edu.silva.crm.domain.dtos.requests;

import com.edu.silva.crm.domain.entities.Item;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record NewBudgetRequestDTO(
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank String terms,
        String observations
) {
}
