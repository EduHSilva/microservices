package com.edu.silva.users.unit.services;

import com.edu.silva.users.domain.dtos.requests.RegisterRequestDTO;
import com.edu.silva.users.domain.dtos.requests.UpdateUserRequestDTO;
import com.edu.silva.users.domain.dtos.responses.UserResponseDTO;
import com.edu.silva.users.domain.entities.Company;
import com.edu.silva.users.domain.entities.User;
import com.edu.silva.users.domain.enums.UserRole;
import com.edu.silva.users.domain.enums.UserStatus;
import com.edu.silva.users.domain.producers.UserProducer;
import com.edu.silva.users.infra.exceptions.CustomExceptions;
import com.edu.silva.users.repositories.CompanyRepository;
import com.edu.silva.users.repositories.UserRepository;
import com.edu.silva.users.services.impl.UserServiceImpl;
import org.checkerframework.checker.units.qual.C;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository repository;
    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private UserProducer producer;

    @InjectMocks
    private UserServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveUserSuccessfully() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "edu@test.com", "Eduardo", "123", UserRole.CRM, null
        );

        when(repository.findByEmail("edu@test.com")).thenReturn(null);
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDTO saved = service.save(dto);

        assertEquals("Eduardo", saved.getUsername());
        assertEquals(UserStatus.WAITING_CONFIRMATION, saved.getStatus());

        verify(producer, times(1)).publishMessageEmail(any(User.class));
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void shouldSaveUserSuccessfullyWithCompany() {
        UUID companyID = UUID.randomUUID();
        Company company = new Company();
        company.setId(companyID);
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "edu@test.com", "Eduardo", "123", UserRole.CRM, companyID
        );

        when(repository.findByEmail("edu@test.com")).thenReturn(null);
        when(companyRepository.findById(companyID)).thenReturn(Optional.of(company));
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDTO saved = service.save(dto);

        assertEquals("Eduardo", saved.getUsername());
        assertEquals(UserStatus.WAITING_CONFIRMATION, saved.getStatus());

        verify(producer, times(1)).publishMessageEmail(any(User.class));
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowWhenCompanyNotFound() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "edu@test.com", "Eduardo", "123", UserRole.CRM, UUID.randomUUID()
        );

        when(companyRepository.findById(UUID.randomUUID())).thenReturn(Optional.empty());

        assertThrows(CustomExceptions.EntityNotFoundException.class, () -> service.save(dto));
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "edu@test.com", "Eduardo", "123", UserRole.CRM, null
        );

        when(repository.findByEmail("edu@test.com")).thenReturn(new User());

        assertThrows(CustomExceptions.EmailAlreadyExistsException.class, () -> service.save(dto));
    }

    @Test
    void shouldThrowWhenUserRegistersAsAdmin() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "Eduardo", "edu@test.com", "123", UserRole.ADMIN, null
        );

        assertThrows(CustomExceptions.InvalidRoleException.class, () -> service.save(dto));
    }

    @Test
    void shouldReturnUserWhenFound() {
        UUID id = UUID.randomUUID();
        User user = new User("Eduardo", "edu@test.com", "123", Collections.singletonList(UserRole.CRM));
        Company company = new Company();
        company.setName("test");
        user.setCompany(company);
        when(repository.findById(id)).thenReturn(Optional.of(user));

        UserResponseDTO result = service.findById(id);
        assertEquals("Eduardo", result.getUsername());
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomExceptions.EntityNotFoundException.class, () -> service.findById(id));
    }

    @Test
    void shouldUpdateUser() {
        UUID id = UUID.randomUUID();

        User user = new User("Old", "old@test.com", "123", Collections.singletonList(UserRole.CRM));
        Company company = new Company();
        company.setName("Teste");
        user.setCompany(company);
        UpdateUserRequestDTO req = new UpdateUserRequestDTO("NewName");

        when(repository.findById(id)).thenReturn(Optional.of(user));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDTO updated = service.update(id, req);

        assertEquals("NewName", updated.getUsername());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingUser() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        UpdateUserRequestDTO req = new UpdateUserRequestDTO("Test");

        assertThrows(CustomExceptions.EntityNotFoundException.class, () -> service.update(id, req));
    }

    @Test
    void shouldConfirmUserSuccessfully() {
        UUID id = UUID.randomUUID();
        User user = new User("Edu", "edu@test.com", "123", Collections.singletonList(UserRole.CRM));
        user.setStatus(UserStatus.WAITING_CONFIRMATION);
        Company company = new Company();
        company.setName("test");
        user.setCompany(company);
        when(repository.findById(id)).thenReturn(Optional.of(user));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDTO confirmed = service.confirm(id);

        assertEquals(UserStatus.OK, confirmed.getStatus());
    }

    @Test
    void shouldNotConfirmWhenStatusInvalid() {
        UUID id = UUID.randomUUID();
        User user = new User("Edu", "edu@test.com", "123", Collections.singletonList(UserRole.CRM));
        user.setStatus(UserStatus.OK);

        when(repository.findById(id)).thenReturn(Optional.of(user));

        assertThrows(CustomExceptions.InvalidStatusException.class, () -> service.confirm(id));
    }

    @Test
    void shouldDeleteUserById() {
        UUID id = UUID.randomUUID();

        service.delete(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void shouldReturnPagedUsers() {
        int page = 0;
        int size = 2;

        Pageable pageable = PageRequest.of(page, size);

        User user1 = new User();
        User user2 = new User();

        Company company = new Company();
        company.setName("test");
        user1.setCompany(company);
        user2.setCompany(company);
        List<User> users = List.of(user1, user2);

        Page<User> userPage = new PageImpl<>(users, pageable, users.size());

        when(repository.findAll(pageable)).thenReturn(userPage);

        User user = new User();
        user.getRoles().add(UserRole.ADMIN);
        Page<UserResponseDTO> result = service.findAll(page, size);

        assertEquals(2, result.getContent().size());
        verify(repository).findAll(pageable);
    }
}
