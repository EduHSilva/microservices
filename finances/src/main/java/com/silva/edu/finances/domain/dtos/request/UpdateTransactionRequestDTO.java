package com.silva.edu.finances.domain.dtos.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.Date;

public record UpdateTransactionRequestDTO(
        double value,
        @NotEmpty String title,
        Date executionDate
) {

}
