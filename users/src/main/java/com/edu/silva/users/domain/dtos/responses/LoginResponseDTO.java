package com.edu.silva.users.domain.dtos.responses;

import java.util.UUID;

public record LoginResponseDTO(UUID id, String username, String email, String token) {
}
