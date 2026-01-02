package com.silva.edu.finances.domain.dtos.request;

import com.silva.edu.finances.domain.enums.RecurrenceType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddRecurrenceRequestDTO(
        @NotNull int day,
        boolean income,
        @NotNull UUID categoryID,
        @NotNull RecurrenceType type,
        Integer installments,
        Integer payed,
        @NotNull double value,
        @NotNull String title
) {
}
