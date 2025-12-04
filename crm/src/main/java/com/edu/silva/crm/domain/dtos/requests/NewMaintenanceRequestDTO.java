package com.edu.silva.crm.domain.dtos.requests;

import com.edu.silva.crm.domain.enums.ServicePriority;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.UUID;

public record NewMaintenanceRequestDTO(
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank Double value,
        Date initialDate,
        Date finalDate,
        ServicePriority priority,
        String observations,
        UUID budget
) {
}
