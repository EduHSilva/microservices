package com.edu.silva.users.unit.controllers;

import com.edu.silva.users.controllers.AuthController;
import com.edu.silva.users.domain.dtos.requests.AuthRequestDTO;
import com.edu.silva.users.domain.entities.User;
import com.edu.silva.users.infra.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private RestTestClient client;

    @Mock
    private TokenService service;

    @Mock
    private AuthenticationManager manager;

    @BeforeEach
    void setUp() {
        client = RestTestClient.bindToController(new AuthController(manager, service))
                .defaultHeader("ContentType", "application/json")
                .build();
    }

    @Test
    void shouldLogin() {
        AuthRequestDTO request = new AuthRequestDTO("test@email.com", "password123");
        User user = new User();

        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());

        when(manager.authenticate(any())).thenReturn(auth);
        when(service.generateToken(user)).thenReturn("fake-jwt-token");

        client.post()
                .uri("/auth/login")
                .body(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Auth successfully verified")
                .jsonPath("$.data").isNotEmpty();
    }

    @Test
    void shouldInvalidLogin() {
        AuthRequestDTO request = new AuthRequestDTO("test@email.com", "password123");
        User user = new User();

        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());

        when(manager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));

        client.post()
                .uri("/auth/login")
                .body(request)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @WithMockUser
    void shouldReturnLoggedUser() {
        client.get()
                .uri("/auth/me")
                .exchange()
                .expectStatus().isOk();
    }
}