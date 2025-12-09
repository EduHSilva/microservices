package com.edu.silva.crm.services.impl;

import com.edu.silva.common.enums.Status;
import com.edu.silva.crm.domain.dtos.requests.NewBudgetRequestDTO;
import com.edu.silva.crm.domain.dtos.requests.UpdateBudgetRequestDTO;
import com.edu.silva.crm.domain.dtos.responses.BudgetResponseDTO;
import com.edu.silva.crm.domain.dtos.ItemDTO;
import com.edu.silva.crm.domain.dtos.responses.ClientResponseDTO;
import com.edu.silva.crm.domain.entities.Budget;
import com.edu.silva.crm.domain.entities.Client;
import com.edu.silva.crm.domain.entities.Item;
import com.edu.silva.crm.domain.enums.BudgetStatus;
import com.edu.silva.crm.domain.enums.ClientStatus;
import com.edu.silva.crm.infra.config.UserContext;
import com.edu.silva.crm.infra.exceptions.CustomExceptions;
import com.edu.silva.crm.repositories.BudgetRepository;
import com.edu.silva.crm.repositories.ClientRepository;
import com.edu.silva.crm.services.BudgetService;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository repository;
    private final ClientRepository clientRepository;

    public BudgetServiceImpl(BudgetRepository repository, ClientRepository clientRepository) {
        this.repository = repository;
        this.clientRepository = clientRepository;
    }

    @Transactional
    public BudgetResponseDTO save(NewBudgetRequestDTO dto) {
        Budget budget = new Budget();
        budget.setStatus(BudgetStatus.NEW);
        UUID userId = UserContext.getUser();
        budget.setUserId(userId);
        budget.setTitle(dto.title());
        budget.setDescription(dto.description());
        budget.setObservations(dto.observations());
        budget.setValidate(dto.validate());
        budget.setTerms(dto.terms());

        Client c = clientRepository.findById(dto.client()).orElseThrow(() -> new CustomExceptions.EntityNotFoundException("Client nao encontrado", ""));
        budget.setClient(c);
        budget.setStatus(BudgetStatus.NEW);
        dto.items().forEach(i -> {
            budget.getItems().add(createItem(i, budget));
        });
        repository.save(budget);

        return new BudgetResponseDTO(budget);
    }

    @Override
    public void delete(UUID id) {
        repository.findById(id).ifPresent(entity -> repository.deleteById(id));
    }

    @Override
    public Page<@NonNull BudgetResponseDTO> findAll(int page, int size, String title, String status) {
        Pageable pageable = PageRequest.of(page, size);

        if (title != null && status != null) {
            return repository
                    .findByTitleContainingIgnoreCaseAndStatus(title, BudgetStatus.valueOf(status.toUpperCase()), pageable)
                    .map(BudgetResponseDTO::new);
        }

        if (title != null) {
            return repository
                    .findByTitleContainingIgnoreCase(title, pageable)
                    .map(BudgetResponseDTO::new);
        }

        if (status != null) {
            return repository
                    .findByStatus(BudgetStatus.valueOf(status.toUpperCase()), pageable)
                    .map(BudgetResponseDTO::new);
        }
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
                    if (dto.status() != null) budget.setStatus(dto.status());
                    budget.setTitle(dto.title());
                    budget.setDescription(dto.description());
                    budget.setValidate(dto.validate());
                    budget.setTerms(dto.terms());
                    budget.setObservations(dto.observations());

                    Map<UUID, Item> existing = budget.getItems().stream()
                            .collect(Collectors.toMap(Item::getId, i -> i));

                    List<UUID> dtoItemIds = dto.items().stream()
                            .map(ItemDTO::getId)
                            .filter(Objects::nonNull)
                            .toList();

                    budget.getItems().removeIf(item ->
                            item.getId() != null && !dtoItemIds.contains(item.getId())
                    );

                    for (ItemDTO i : dto.items()) {
                        if (i.getId() != null && existing.containsKey(i.getId())) {
                            Item item = existing.get(i.getId());
                            item.setName(i.getName());
                            item.setDescription(i.getDescription());
                            item.setQuantity(i.getQuantity());
                            item.setPriceUn(i.getPrice());
                            item.setStatus(Status.ACTIVE);
                        } else {
                            budget.getItems().add(createItem(i, budget));
                        }
                    }

                    return repository.save(budget);
                })
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(Budget.class.getSimpleName(), id)));
    }

    private Item createItem(ItemDTO i, Budget budget) {
        Item item = new Item();
        item.setBudget(budget);
        item.setName(i.getName());
        item.setDescription(i.getDescription());
        item.setQuantity(i.getQuantity());
        item.setPriceUn(i.getPrice());
        item.setStatus(Status.ACTIVE);
        return item;
    }
}
