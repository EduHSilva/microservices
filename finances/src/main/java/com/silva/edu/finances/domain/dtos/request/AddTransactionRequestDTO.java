package com.silva.edu.finances.domain.dtos.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.UUID;

public record AddTransactionRequestDTO(
        boolean income,
        double value,
        @NotNull UUID categoryID,
        @NotEmpty String title,
        UUID recurrencyID,
        @NotNull Date executionDate
) {
}
