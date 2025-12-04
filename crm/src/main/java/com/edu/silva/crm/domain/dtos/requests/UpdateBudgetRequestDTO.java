package com.edu.silva.crm.domain.dtos.requests;

import com.edu.silva.crm.domain.enums.BudgetStatus;

public record UpdateBudgetRequestDTO(
        BudgetStatus status
) {
}
