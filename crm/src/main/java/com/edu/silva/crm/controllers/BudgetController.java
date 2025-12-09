package com.edu.silva.crm.controllers;

import com.edu.silva.common.DefaultResponse;
import com.edu.silva.crm.domain.dtos.requests.NewBudgetRequestDTO;
import com.edu.silva.crm.domain.dtos.requests.UpdateBudgetRequestDTO;
import com.edu.silva.crm.domain.dtos.responses.BudgetResponseDTO;
import com.edu.silva.crm.services.BudgetService;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class BudgetController {

    private final String BASE_URL = "/crm/budgets";

    private final BudgetService service;

    public BudgetController(BudgetService service) {
        this.service = service;
    }

    @GetMapping("/budgets")
    public ResponseEntity<@NonNull DefaultResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status
    ) {
        Page<@NonNull BudgetResponseDTO> budgets = service.findAll(page, size, title, status);

        return ResponseEntity.ok(
                new DefaultResponse("Find all budgets successfully", budgets, BASE_URL)
        );
    }

    @PostMapping("/budgets")
    public ResponseEntity<@NonNull DefaultResponse> add(
            @RequestBody @Valid NewBudgetRequestDTO dto
    ) {
        BudgetResponseDTO budget = service.save(dto);

        return ResponseEntity.ok(
                new DefaultResponse("Budget successfully saved", budget, BASE_URL, budget.getId())
        );
    }

    @GetMapping("/budgets/{id}")
    public ResponseEntity<@NonNull DefaultResponse> get(
            @PathVariable UUID id
    ) {
        BudgetResponseDTO dto = service.findById(id);
        return ResponseEntity.ok(
                new DefaultResponse("Budget successfully retrieved", dto, BASE_URL, id)
        );
    }

    @DeleteMapping("/budgets/{id}")
    public ResponseEntity<@NonNull DefaultResponse> delete(
            @PathVariable UUID id
    ) {
        service.delete(id);
        return ResponseEntity.ok(
                new DefaultResponse("Budget successfully deleted")
        );
    }

    @PutMapping("/budgets/{id}")
    public ResponseEntity<@NonNull DefaultResponse> update(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateBudgetRequestDTO dto
    ) {
        return ResponseEntity.ok(
                new DefaultResponse("Budget successfully saved", service.update(id, dto), BASE_URL, id)
        );
    }
}
