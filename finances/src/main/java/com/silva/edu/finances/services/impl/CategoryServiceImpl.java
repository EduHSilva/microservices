package com.silva.edu.finances.services.impl;

import com.silva.edu.finances.domain.dtos.request.AddCategoryRequestDTO;
import com.silva.edu.finances.domain.dtos.request.AddTransactionRequestDTO;
import com.silva.edu.finances.domain.dtos.request.UpdateCategoryRequestDTO;
import com.silva.edu.finances.domain.dtos.request.UpdateTransactionRequestDTO;
import com.silva.edu.finances.domain.dtos.response.CategoryResponseDTO;
import com.silva.edu.finances.domain.dtos.response.TransactionResponseDTO;
import com.silva.edu.finances.domain.entities.Category;
import com.silva.edu.finances.domain.entities.Transaction;
import com.silva.edu.finances.domain.enums.CategoryStatus;
import com.silva.edu.finances.domain.enums.TransactionStatus;
import com.silva.edu.finances.infra.exceptions.CustomExceptions;
import com.silva.edu.finances.repositories.CategoryRepository;
import com.silva.edu.finances.repositories.TransactionRepository;
import com.silva.edu.finances.services.CategoryService;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public CategoryResponseDTO save(AddCategoryRequestDTO request) {
        Category category = new Category();
        BeanUtils.copyProperties(request, category);
        category.setStatus(CategoryStatus.ACTIVE);
        repository.save(category);
        return new CategoryResponseDTO(category);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Page<@NonNull CategoryResponseDTO> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable).map(CategoryResponseDTO::new);
    }

    @Override
    public CategoryResponseDTO findById(UUID id) {
        return new CategoryResponseDTO(repository.findById(id)
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(Category.class.getSimpleName(), id)));
    }

    @Override
    public CategoryResponseDTO update(UUID id, UpdateCategoryRequestDTO request) {
        return repository.findById(id)
                .map(category -> {
                    category.setTitle(request.title());
                    category.setGoal(request.goal());
                    return new CategoryResponseDTO(repository.save(category));
                })
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(Category.class.getSimpleName(), id));
    }
}
