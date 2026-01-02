package com.silva.edu.finances.services.impl;

import com.silva.edu.finances.domain.dtos.request.AddRecurrenceRequestDTO;
import com.silva.edu.finances.domain.dtos.request.UpdateRecurrenceRequestDTO;
import com.silva.edu.finances.domain.dtos.response.RecurrenceResponseDTO;
import com.silva.edu.finances.domain.entities.Category;
import com.silva.edu.finances.domain.entities.Recurrence;
import com.silva.edu.finances.domain.enums.RecurrenceStatus;
import com.silva.edu.finances.domain.enums.RecurrenceType;
import com.silva.edu.finances.infra.exceptions.CustomExceptions;
import com.silva.edu.finances.repositories.CategoryRepository;
import com.silva.edu.finances.repositories.RecurrenceRepository;
import com.silva.edu.finances.services.RecurrenceService;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RecurrenceServiceImpl implements RecurrenceService {

    private final RecurrenceRepository repository;
    private final CategoryRepository categoryRepository;

    public RecurrenceServiceImpl(RecurrenceRepository repository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public RecurrenceResponseDTO save(AddRecurrenceRequestDTO request) {
        Recurrence r = new Recurrence();
        BeanUtils.copyProperties(request, r);
        r.setStatus(RecurrenceStatus.ACTIVE);
        if (request.type() == RecurrenceType.INSTALLMENT && request.installments() == 0) {
            throw new CustomExceptions.InvalidInstallmentsException();
        }

        Category category = categoryRepository.findById(request.categoryID())
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(Category.class.getSimpleName(), request.categoryID()));

        r.setCategory(category);
        repository.save(r);
        return new RecurrenceResponseDTO(r);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Page<@NonNull RecurrenceResponseDTO> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable).map(RecurrenceResponseDTO::new);
    }

    @Override
    public RecurrenceResponseDTO findById(UUID id) {
        return new RecurrenceResponseDTO(repository.findById(id)
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(Recurrence.class.getSimpleName(), id)));
    }

    @Override
    public RecurrenceResponseDTO update(UUID id, UpdateRecurrenceRequestDTO request) {
        return repository.findById(id)
                .map(recurrence -> {
                    recurrence.setDay(request.day());
                    recurrence.setInstallments(request.installments());
                    recurrence.setPayed(request.payed());
                    recurrence.setTitle(request.title());
                    return new RecurrenceResponseDTO(repository.save(recurrence));
                })
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(Recurrence.class.getSimpleName(), id));
    }
}
