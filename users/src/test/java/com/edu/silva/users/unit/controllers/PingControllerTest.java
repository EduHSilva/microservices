package com.edu.silva.users.unit.controllers;

import com.edu.silva.users.controllers.PingController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.client.RestTestClient;

@ExtendWith(MockitoExtension.class)
class PingControllerTest {

    private RestTestClient client;

    @BeforeEach
    void setUp() {
        client = RestTestClient.bindToController(new PingController())
                .defaultHeader("ContentType", "application/json")
                .build();
    }

    @Test
    void shouldReturnAny() {
        client.get()
                .uri("/ping")
                .exchange()
                .expectStatus().isOk()
                .expectBody();
    }
}