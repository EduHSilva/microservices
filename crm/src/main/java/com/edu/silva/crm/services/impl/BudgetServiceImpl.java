package com.edu.silva.crm.services.impl;

import com.edu.silva.crm.domain.dtos.requests.NewBudgetRequestDTO;
import com.edu.silva.crm.domain.dtos.requests.UpdateBudgetRequestDTO;
import com.edu.silva.crm.domain.dtos.responses.BudgetResponseDTO;
import com.edu.silva.crm.domain.entities.Budget;
import com.edu.silva.crm.domain.enums.BudgetStatus;
import com.edu.silva.crm.infra.config.UserContext;
import com.edu.silva.crm.infra.exceptions.CustomExceptions;
import com.edu.silva.crm.repositories.BudgetRepository;
import com.edu.silva.crm.services.BudgetService;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository repository;

    public BudgetServiceImpl(BudgetRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public BudgetResponseDTO save(NewBudgetRequestDTO dto) {
        Budget budget = new Budget(dto.title(), dto.description(), dto.terms(), dto.observations());
        budget.setStatus(BudgetStatus.NEW);
        UUID userId = UserContext.getUser();
        budget.setUserId(userId);
        repository.save(budget);
        return new BudgetResponseDTO(budget);
    }

    @Override
    public void delete(UUID id) {
        repository.findById(id).ifPresent(entity -> {
            repository.deleteById(id);
        });
    }

    @Override
    public Page<@NonNull BudgetResponseDTO> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable).map(BudgetResponseDTO::new);
    }

    @Override
    public BudgetResponseDTO findById(UUID id) {
        return new BudgetResponseDTO(repository.findById(id)
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(Budget.class.getSimpleName(), id)));
    }

    @Override
    public BudgetResponseDTO update(UUID id, UpdateBudgetRequestDTO dto) {
        return new BudgetResponseDTO(repository.findById(id)
                .map(budget -> {
                    budget.setStatus(dto.status());
                    return repository.save(budget);
                })
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(Budget.class.getSimpleName(), id)));
    }
}
