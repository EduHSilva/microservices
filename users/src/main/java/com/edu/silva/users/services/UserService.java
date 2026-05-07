package com.edu.silva.users.services;

import com.edu.silva.users.domain.dtos.requests.AuthRequestDTO;
import com.edu.silva.users.domain.dtos.requests.RegisterRequestDTO;
import com.edu.silva.users.domain.dtos.requests.UpdateUserRequestDTO;
import com.edu.silva.users.domain.dtos.responses.LoginResponseDTO;
import com.edu.silva.users.domain.dtos.responses.UserResponseDTO;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.NonNull;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface UserService {
    UserResponseDTO save(RegisterRequestDTO request);

    void delete(UUID id);

    Page<@NonNull UserResponseDTO> findAll(int page, int size);

    UserResponseDTO findById(UUID id);

    UserResponseDTO update(UUID id, UpdateUserRequestDTO request);

    UserResponseDTO confirm(UUID id);

    LoginResponseDTO login(AuthRequestDTO dto);
}
