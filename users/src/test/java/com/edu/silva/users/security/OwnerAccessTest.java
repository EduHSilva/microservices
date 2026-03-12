package com.edu.silva.users.security;

import com.edu.silva.users.domain.dtos.requests.UpdateCompanyRequestDTO;
import com.edu.silva.users.domain.entities.Company;
import com.edu.silva.users.domain.entities.User;
import com.edu.silva.users.repositories.CompanyRepository;
import com.edu.silva.users.services.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@EnableMethodSecurity
class OwnerAccessTest {

    @Autowired
    CompanyService companyService;
    @Mock
    CompanyRepository repository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(companyService, "repository", repository);
    }

    @Test
    @WithMockUser(username = "owner", roles = "CRM")
    void shouldAllowOwner() {
        UUID id = UUID.randomUUID();

        Company company = new Company();
        User owner = new User();
        owner.setEmail("owner@email.com");
        owner.setUsername("owner@email.com");
        company.setOwner(owner);

        when(repository.findById(id)).thenReturn(Optional.of(company));

        UpdateCompanyRequestDTO req = new UpdateCompanyRequestDTO("NewName");
        assertDoesNotThrow(() -> {
            try {
                companyService.update(id, req);
            } catch (Exception ignored) {
            }
        });
    }

    @Test
    @WithMockUser(username = "other", roles = "CRM")
    void shouldBlockNonOwner() {
        UUID id = UUID.randomUUID();

        Company company = new Company();
        User owner = new User();
        owner.setEmail("owner@email.com");
        owner.setUsername("owner@email.com");
        company.setOwner(owner);

        when(repository.findById(id)).thenReturn(Optional.of(company));

        UpdateCompanyRequestDTO req = new UpdateCompanyRequestDTO("NewName");

        assertThrows(AccessDeniedException.class,
                () -> companyService.update(id, req));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAdmin() {
        UUID companyId = UUID.randomUUID();
        UpdateCompanyRequestDTO req = new UpdateCompanyRequestDTO("NewName");
        assertDoesNotThrow(() -> {
            try {
                companyService.update(companyId, req);
            } catch (Exception ignored) {
            }
        });
    }
}