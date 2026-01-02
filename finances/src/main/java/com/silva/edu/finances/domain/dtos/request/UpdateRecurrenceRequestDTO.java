package com.silva.edu.finances.domain.dtos.request;

import jakarta.validation.constraints.NotNull;

public record UpdateRecurrenceRequestDTO(
        @NotNull int day,
        @NotNull String title,
        Integer installments,
        Integer payed,
        @NotNull double value
) {
}
