package com.edu.silva.crm.services;

import com.edu.silva.crm.domain.dtos.requests.NewMaintenanceRequestDTO;
import com.edu.silva.crm.domain.dtos.requests.UpdateMaintenanceRequestDTO;
import com.edu.silva.crm.domain.dtos.responses.MaintenanceResponseDTO;
import lombok.NonNull;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface MaintenanceService {
    MaintenanceResponseDTO save(NewMaintenanceRequestDTO request);
    void delete(UUID id);
    Page<@NonNull MaintenanceResponseDTO> findAll(int page, int size);
    MaintenanceResponseDTO findById(UUID id);
    MaintenanceResponseDTO update(UUID id, UpdateMaintenanceRequestDTO request);
}
