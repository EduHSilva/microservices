package com.edu.silva.crm.domain.dtos.requests;

import com.edu.silva.crm.domain.enums.ServiceStatus;

public record UpdateMaintenanceRequestDTO(
        ServiceStatus status
) {
}
