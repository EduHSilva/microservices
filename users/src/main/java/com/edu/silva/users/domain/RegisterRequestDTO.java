package com.edu.silva.users.domain;

public record RegisterRequestDTO(
        String email,
        String username,
        String password,
        UserRole role
) {
}
