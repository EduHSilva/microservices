package com.edu.silva.crm.services.impl;

import com.edu.silva.crm.domain.dtos.requests.NewMaintenanceRequestDTO;
import com.edu.silva.crm.domain.dtos.requests.UpdateMaintenanceRequestDTO;
import com.edu.silva.crm.domain.dtos.responses.MaintenanceResponseDTO;
import com.edu.silva.crm.domain.entities.Maintenance;
import com.edu.silva.crm.domain.enums.ServiceStatus;
import com.edu.silva.crm.infra.config.UserContext;
import com.edu.silva.crm.infra.exceptions.CustomExceptions;
import com.edu.silva.crm.repositories.MaintenanceRepository;
import com.edu.silva.crm.services.MaintenanceService;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

    private final MaintenanceRepository repository;

    public MaintenanceServiceImpl(MaintenanceRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public MaintenanceResponseDTO save(NewMaintenanceRequestDTO dto) {
        Maintenance maintenance = new Maintenance();
        BeanUtils.copyProperties(dto, maintenance);
        maintenance.setStatus(ServiceStatus.NEW);
        UUID userId = UserContext.getUser();
        maintenance.setUserId(userId);
        repository.save(maintenance);
        return new MaintenanceResponseDTO(maintenance);
    }

    @Override
    public void delete(UUID id) {
        repository.findById(id).ifPresent(entity -> repository.deleteById(id));
    }

    @Override
    public Page<@NonNull MaintenanceResponseDTO> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable).map(MaintenanceResponseDTO::new);
    }

    @Override
    public MaintenanceResponseDTO findById(UUID id) {
        return new MaintenanceResponseDTO(repository.findById(id)
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(Maintenance.class.getSimpleName(), id)));
    }

    @Override
    public MaintenanceResponseDTO update(UUID id, UpdateMaintenanceRequestDTO dto) {
        return new MaintenanceResponseDTO(repository.findById(id)
                .map(maintenance -> {
                    maintenance.setStatus(dto.status());
                    return repository.save(maintenance);
                })
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(Maintenance.class.getSimpleName(), id)));
    }
}
