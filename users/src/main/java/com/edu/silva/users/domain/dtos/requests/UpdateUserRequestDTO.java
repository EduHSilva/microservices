package com.edu.silva.users.domain.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para atualizar usuário")
public record UpdateUserRequestDTO(
        @Schema(description = "Nome do usuário", example = "Eduardo Henrique da Silva")
        @NotBlank String username
) {
}
