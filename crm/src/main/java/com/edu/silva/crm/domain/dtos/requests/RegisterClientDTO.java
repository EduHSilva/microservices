package com.edu.silva.crm.domain.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterClientDTO(
        @NotBlank @Email String email,
        @NotBlank String name,
        @NotBlank String phone,
        String observations
) {
}
