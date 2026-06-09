package com.edu.silva.users.domain.dtos.requests;

import com.edu.silva.users.domain.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Schema(description = "Dados para registrar usuário")
public record RegisterRequestDTO(
        @Schema(description = "E-mail", example = "edu.slhenrique@gmail.com")
        @NotBlank @Email String email,
        @Schema(description = "Nome completo", example = "Eduardo Silva")
        @NotBlank String username,
        @Schema(description = "Senha", example = "dsfafiafhah11@")
        @NotBlank String password,
        @Schema(description = "Perfil de usuário", example = "crm")
        UserRole role,
        @Schema(description = "ID da empresa do usuário", example = "1")
        UUID companyID
) {
}
