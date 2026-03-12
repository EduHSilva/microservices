package com.edu.silva.users.services.impl;

import com.edu.silva.users.domain.dtos.requests.UpdateCompanyRequestDTO;
import com.edu.silva.users.domain.dtos.responses.CompanyResponseDTO;
import com.edu.silva.users.domain.entities.Company;
import com.edu.silva.users.infra.exceptions.CustomExceptions;
import com.edu.silva.users.infra.security.annotattions.OnlyOwner;
import com.edu.silva.users.repositories.CompanyRepository;
import com.edu.silva.users.services.CompanyService;
import com.edu.silva.users.infra.security.annotattions.OnlyAdmin;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository repository;

    public CompanyServiceImpl(CompanyRepository repository) {
        this.repository = repository;
    }

    @Override
    @OnlyAdmin
    public Page<@NonNull CompanyResponseDTO> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable).map(CompanyResponseDTO::new);
    }

    @Override
    @OnlyAdmin
    public CompanyResponseDTO findById(UUID id) {
        return new CompanyResponseDTO(repository.findById(id)
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(Company.class.getSimpleName(), id)));
    }

    @Override
    @OnlyOwner
    public CompanyResponseDTO update(UUID id, UpdateCompanyRequestDTO updateRequest) {
        return repository.findById(id)
                .map(company -> {
                    company.setName(updateRequest.name());
                    return new CompanyResponseDTO(repository.save(company));
                })
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(Company.class.getSimpleName(), id));
    }
}
