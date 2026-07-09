package com.edu.silva.users.domain.dtos.requests;

import jakarta.validation.constraints.Email;
public record AuthRequestDTO(
        @Email String email,
        String password,
        String tokenGmail) {
}
