package com.edu.silva.crm.controllers;

import com.edu.silva.common.DefaultResponse;
import com.edu.silva.crm.domain.dtos.requests.NewMaintenanceRequestDTO;
import com.edu.silva.crm.domain.dtos.requests.UpdateMaintenanceRequestDTO;
import com.edu.silva.crm.domain.dtos.responses.MaintenanceResponseDTO;
import com.edu.silva.crm.services.MaintenanceService;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class MaintenanceController {

    private final String BASE_URL = "/crm/maintenances";

    private final MaintenanceService service;

    public MaintenanceController(MaintenanceService service) {
        this.service = service;
    }

    @GetMapping("/maintenances")
    public ResponseEntity<@NonNull DefaultResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<@NonNull MaintenanceResponseDTO> dtos = service.findAll(page, size);

        return ResponseEntity.ok(
                new DefaultResponse("Find all maintenances successfully", dtos, BASE_URL)
        );
    }

    @PostMapping("/maintenances")
    public ResponseEntity<@NonNull DefaultResponse> add(
            @RequestBody @Valid NewMaintenanceRequestDTO dto
    ) {
        MaintenanceResponseDTO maintenance = service.save(dto);

        return ResponseEntity.ok(
                new DefaultResponse("maintenance successfully saved", maintenance, BASE_URL, maintenance.getId())
        );
    }

    @GetMapping("/maintenances/{id}")
    public ResponseEntity<@NonNull DefaultResponse> get(
            @PathVariable UUID id
    ) {
        MaintenanceResponseDTO dto = service.findById(id);
        return ResponseEntity.ok(
                new DefaultResponse("maintenance successfully retrieved", dto, BASE_URL, id)
        );
    }

    @DeleteMapping("/maintenances/{id}")
    public ResponseEntity<@NonNull DefaultResponse> delete(
            @PathVariable UUID id
    ) {
        service.delete(id);
        return ResponseEntity.ok(
                new DefaultResponse("maintenance successfully deleted")
        );
    }

    @PutMapping("/maintenances/{id}")
    public ResponseEntity<@NonNull DefaultResponse> update(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateMaintenanceRequestDTO dto
    ) {
        return ResponseEntity.ok(
                new DefaultResponse("maintenance successfully saved", service.update(id, dto), BASE_URL, id)
        );
    }
}
