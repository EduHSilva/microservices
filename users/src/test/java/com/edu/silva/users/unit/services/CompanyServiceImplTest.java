package com.edu.silva.users.unit.services;

import com.edu.silva.users.domain.dtos.requests.UpdateCompanyRequestDTO;
import com.edu.silva.users.domain.dtos.requests.UpdateUserRequestDTO;
import com.edu.silva.users.domain.dtos.responses.CompanyResponseDTO;
import com.edu.silva.users.domain.dtos.responses.UserResponseDTO;
import com.edu.silva.users.domain.entities.Company;
import com.edu.silva.users.domain.entities.User;
import com.edu.silva.users.domain.enums.UserRole;
import com.edu.silva.users.infra.exceptions.CustomExceptions;
import com.edu.silva.users.repositories.CompanyRepository;
import com.edu.silva.users.services.impl.CompanyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CompanyServiceImplTest {

    @Mock
    private CompanyRepository repository;

    @InjectMocks
    private CompanyServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnPagedCompanies() {
        int page = 0;
        int size = 2;

        Pageable pageable = PageRequest.of(page, size);

        Company company = new Company();
        Company company1 = new Company();

        List<Company> companies = List.of(company, company1);

        Page<Company> pageComp = new PageImpl<>(companies, pageable, companies.size());

        when(repository.findAll(pageable)).thenReturn(pageComp);

        Page<CompanyResponseDTO> result = service.findAll(page, size);

        assertEquals(2, result.getContent().size());
        verify(repository).findAll(pageable);
    }

    @Test
    void shouldReturnCompany() {
        int page = 0;
        int size = 2;

        Pageable pageable = PageRequest.of(page, size);

        Company company = new Company();
        Company company1 = new Company();

        List<Company> companies = List.of(company, company1);

        Page<Company> pageComp = new PageImpl<>(companies, pageable, companies.size());

        when(repository.findAll(pageable)).thenReturn(pageComp);

        Page<CompanyResponseDTO> result = service.findAll(page, size);

        assertEquals(2, result.getContent().size());
        verify(repository).findAll(pageable);
    }

    @Test
    void shouldReturnCompanyWhenFound() {
        UUID id = UUID.randomUUID();
        Company company = new Company();
        when(repository.findById(id)).thenReturn(Optional.of(company));

        CompanyResponseDTO result = service.findById(id);
        assertNotNull(result);
    }

    @Test
    void shouldThrowWhenCompanyNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomExceptions.EntityNotFoundException.class, () -> service.findById(id));
    }

    @Test
    void shouldUpdateCompany() {
        UUID id = UUID.randomUUID();

        Company company = new Company();
        company.setName("Old");
        UpdateCompanyRequestDTO req = new UpdateCompanyRequestDTO("NewName");

        when(repository.findById(id)).thenReturn(Optional.of(company));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CompanyResponseDTO updated = service.update(id, req);

        assertEquals("NewName", updated.getName());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingCompany() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        UpdateCompanyRequestDTO req = new UpdateCompanyRequestDTO("Test");

        assertThrows(CustomExceptions.EntityNotFoundException.class, () -> service.update(id, req));
    }
}
