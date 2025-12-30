package com.silva.edu.finances.domain.dtos.request;

import com.silva.edu.finances.domain.enums.CategoryStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateCategoryRequestDTO(
        @NotEmpty String title,
        @NotNull int goal,
        @NotEmpty String color) {

}
