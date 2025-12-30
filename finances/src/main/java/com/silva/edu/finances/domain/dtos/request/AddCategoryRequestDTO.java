package com.silva.edu.finances.domain.dtos.request;

import com.silva.edu.finances.domain.enums.CategoryClassification;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AddCategoryRequestDTO(
        @NotEmpty String title,
        @NotEmpty String color,
        @NotNull int goal,
        @NotNull CategoryClassification classification
        ) {
}
