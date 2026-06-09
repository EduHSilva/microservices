package com.edu.silva.users.domain.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

@Schema(description= "Dados para realizar login")
public record AuthRequestDTO(
        @Schema(description = "E-mail", example = "edu.slhenrique@gmail.com")
        @Email String email,
        @Schema(description = "Senha", example = "dsfafiafhah11@")
        String password,
        @Schema(description = "Token gmail", example = "iofanoifoadnfacniozxoncioad")
        String tokenGmail) {
}
