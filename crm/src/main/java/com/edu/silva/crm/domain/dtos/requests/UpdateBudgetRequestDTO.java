package com.edu.silva.crm.domain.dtos.requests;

import com.edu.silva.crm.domain.dtos.ItemDTO;
import com.edu.silva.crm.domain.enums.BudgetStatus;

import java.util.List;

public record UpdateBudgetRequestDTO(
        String title,
        String description,
        int validate,
        List<ItemDTO> items,
        String terms,
        String observations,
        BudgetStatus status
) {
}
