package com.edu.silva.users.domain.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record LoginResponseDTO(
        @Schema(description = "UUID", example = "45c49cf1-2eef-4896-a2fd-02fd7380a5f0")
        UUID id,
        @Schema(description = "Nome", example = "Eduardo Henrique")
        String username,
        @Schema(description = "E-mail", example = "edu.slhenrique@gmail.com")
        String email,
        @Schema(description = "Token", example = "728e79b17e4054708856913feb89296892db5721e21d0ab83604b17ea646b6fe")
        String token) {
}
