package com.edu.silva.users.domain.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para atualizar empresa")
public record UpdateCompanyRequestDTO(
        @Schema(description = "Nome da empresa", example = "Eduardo Silva LTDA")
        @NotBlank String name
) {
}
