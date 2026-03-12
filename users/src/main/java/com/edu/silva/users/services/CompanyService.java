package com.edu.silva.users.services;

import com.edu.silva.users.domain.dtos.requests.UpdateCompanyRequestDTO;
import com.edu.silva.users.domain.dtos.requests.UpdateUserRequestDTO;
import com.edu.silva.users.domain.dtos.responses.CompanyResponseDTO;
import com.edu.silva.users.domain.dtos.responses.UserResponseDTO;
import com.edu.silva.users.domain.entities.User;
import lombok.NonNull;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface CompanyService {
    Page<@NonNull CompanyResponseDTO> findAll(int page, int size);

    CompanyResponseDTO findById(UUID id);

    CompanyResponseDTO update(UUID id, UpdateCompanyRequestDTO request);
}
