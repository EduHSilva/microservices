package com.edu.silva.crm.services;

import com.edu.silva.crm.domain.dtos.requests.NewBudgetRequestDTO;
import com.edu.silva.crm.domain.dtos.requests.UpdateBudgetRequestDTO;
import com.edu.silva.crm.domain.dtos.responses.BudgetResponseDTO;
import lombok.NonNull;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface BudgetService {
    BudgetResponseDTO save(NewBudgetRequestDTO request);
    void delete(UUID id);
    Page<@NonNull BudgetResponseDTO> findAll(int page, int size, String title, String status);
    BudgetResponseDTO findById(UUID id);
    BudgetResponseDTO update(UUID id, UpdateBudgetRequestDTO request);
}
