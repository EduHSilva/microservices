package com.edu.silva.users.unit.controllers;

import com.edu.silva.users.controllers.UserController;
import com.edu.silva.users.domain.dtos.requests.RegisterRequestDTO;
import com.edu.silva.users.domain.dtos.requests.UpdateUserRequestDTO;
import com.edu.silva.users.domain.dtos.responses.UserResponseDTO;
import com.edu.silva.users.domain.entities.User;
import com.edu.silva.users.domain.enums.UserRole;
import com.edu.silva.users.services.UserService;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private RestTestClient client;

    @Mock
    private UserService service;

    @BeforeEach
    void setUp() {
        client = RestTestClient.bindToController(new UserController(service))
                .defaultHeader("ContentType", "application/json")
                .build();
    }

    @Test
    void shouldReturnUsers() {
        List<UserResponseDTO> users = List.of(new UserResponseDTO());
        Page<UserResponseDTO> page =
                new PageImpl<>(users, PageRequest.of(0, 10), users.size());

        when(service.findAll(0, 10)).thenReturn(page);

        client.get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Find all users successfully")
                .jsonPath("$.data.items").isArray();
    }

    @Test
    void shouldFindUser() {
        UUID userId = UUID.randomUUID();

        when(service.findById(userId)).thenReturn(new UserResponseDTO());

        client.get()
                .uri("/" + userId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("User find successfully")
                .jsonPath("$.data").isNotEmpty();
    }

    @Test
    void shouldUpdateUser() {
        UUID userId = UUID.randomUUID();
        UpdateUserRequestDTO requestDTO = new UpdateUserRequestDTO(RandomStringUtils.random(10));
        when(service.update(userId, requestDTO)).thenReturn(new UserResponseDTO());

        client.put()
                .uri("/" + userId)
                .body(requestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("User successfully updated")
                .jsonPath("$.data").isNotEmpty();
    }

    @Test
    void shouldDeletedUser() {
        UUID userId = UUID.randomUUID();

        client.delete()
                .uri("/" + userId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("User successfully deleted")
                .jsonPath("$.data").isEmpty();
    }

    @Test
    void shouldConfirmEmail() {
        UUID userId = UUID.randomUUID();

        client.get()
                .uri("/confirm/" + userId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", "/confirm.html");    }

    @Test
    void shouldSaveUser() {
        RegisterRequestDTO requestDTO = new RegisterRequestDTO(
                "admin@gmail.com", RandomStringUtils.random(10),
                RandomStringUtils.random(10), UserRole.CRM, null);
        when(service.save(requestDTO)).thenReturn(new UserResponseDTO());

        client.post()
                .uri("/")
                .body(requestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.message").isEqualTo("User successfully saved")
                .jsonPath("$.data").isNotEmpty();
    }
}