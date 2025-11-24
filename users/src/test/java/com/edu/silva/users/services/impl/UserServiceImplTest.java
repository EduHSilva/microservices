package com.edu.silva.users.services.impl;

import com.edu.silva.users.domain.dtos.requests.RegisterRequestDTO;
import com.edu.silva.users.domain.dtos.requests.UpdateUserRequestDTO;
import com.edu.silva.users.domain.entities.User;
import com.edu.silva.users.domain.enums.UserRole;
import com.edu.silva.users.domain.enums.UserStatus;
import com.edu.silva.users.domain.producers.UserProducer;
import com.edu.silva.users.infra.exceptions.CustomExceptions;
import com.edu.silva.users.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository repository;

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
                "edu@test.com", "Eduardo", "123", UserRole.CRM
        );

        when(repository.findByEmail("edu@test.com")).thenReturn(null);
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User saved = service.save(dto);

        assertEquals("Eduardo", saved.getUsername());
        assertEquals(UserStatus.WAITING_CONFIRMATION, saved.getStatus());

        verify(producer, times(1)).publishMessageEmail(any(User.class));
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "edu@test.com", "Eduardo", "123", UserRole.CRM
        );

        when(repository.findByEmail("edu@test.com")).thenReturn(new User());

        assertThrows(CustomExceptions.EmailAlreadyExistsException.class, () -> service.save(dto));
    }

    @Test
    void shouldThrowWhenUserRegistersAsAdmin() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "Eduardo", "edu@test.com", "123", UserRole.ADMIN
        );

        assertThrows(CustomExceptions.InvalidRoleException.class, () -> service.save(dto));
    }

    @Test
    void shouldReturnUserWhenFound() {
        UUID id = UUID.randomUUID();
        User user = new User("Eduardo", "edu@test.com", "123", UserRole.CRM);
        when(repository.findById(id)).thenReturn(Optional.of(user));

        User result = service.findById(id);
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

        User user = new User("Old", "old@test.com", "123", UserRole.CRM);
        UpdateUserRequestDTO req = new UpdateUserRequestDTO("NewName");

        when(repository.findById(id)).thenReturn(Optional.of(user));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        User updated = service.update(id, req);

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
        User user = new User("Edu", "edu@test.com", "123", UserRole.CRM);
        user.setStatus(UserStatus.WAITING_CONFIRMATION);

        when(repository.findById(id)).thenReturn(Optional.of(user));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        User confirmed = service.confirm(id);

        assertEquals(UserStatus.OK, confirmed.getStatus());
    }

    @Test
    void shouldNotConfirmWhenStatusInvalid() {
        UUID id = UUID.randomUUID();
        User user = new User("Edu", "edu@test.com", "123", UserRole.CRM);
        user.setStatus(UserStatus.OK);

        when(repository.findById(id)).thenReturn(Optional.of(user));

        assertThrows(CustomExceptions.InvalidStatusException.class, () -> service.confirm(id));
    }
}
