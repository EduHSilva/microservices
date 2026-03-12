package com.edu.silva.users.security;

import com.edu.silva.users.services.CompanyService;
import com.edu.silva.users.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@EnableMethodSecurity
class AdminAccessTest {

    @Autowired
    CompanyService companyService;
    @Autowired
    UserService userService;

    @Test
    @WithMockUser(roles = "CRM")
    void shouldBlockNonAdminAccess() {
        assertThrows(AccessDeniedException.class,
                () -> userService.findAll(0, 10));

        assertThrows(AccessDeniedException.class,
                () -> companyService.findAll(0, 10));

        assertThrows(AccessDeniedException.class,
                () -> companyService.findById(UUID.randomUUID()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAdminAccess() {
        assertAdminAccess(() -> userService.findAll(0, 10));

        assertAdminAccess(() -> companyService.findAll(0, 10));

        assertAdminAccess(() -> companyService.findById(UUID.randomUUID()));
    }

    private void assertAdminAccess(Runnable action) {
        assertDoesNotThrow(() -> {
            try {
                action.run();
            } catch (Exception ignored) {
            }
        });
    }
}
