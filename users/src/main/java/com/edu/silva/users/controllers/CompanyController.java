package com.edu.silva.users.controllers;

import com.edu.silva.common.DefaultResponse;
import com.edu.silva.users.domain.dtos.requests.UpdateCompanyRequestDTO;
import com.edu.silva.users.domain.dtos.responses.CompanyResponseDTO;
import com.edu.silva.users.services.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Empresa")
@RestController()
public class CompanyController {
    private final CompanyService service;

    private final String BASE_URL = "/companies";

    public CompanyController(CompanyService service) {
        this.service = service;
    }

    @Operation(
            summary = "Buscar todas empresas",
            description = "Esta rota é utilizada no admin para buscar todas empresas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empresas recuperadas com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não possui permissão")
    })
    @GetMapping(BASE_URL)
    ResponseEntity<@NonNull DefaultResponse> all(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<@NonNull CompanyResponseDTO> company = service.findAll(page, size);
        return ResponseEntity.ok(new DefaultResponse("Find all companies successfully", company, BASE_URL));
    }

    @Operation(
            summary = "Buscar empresa por id",
            description = "Esta rota é utilizada no admin para buscar detalhes de uma empresa."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empresa recuperada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
    })
    @GetMapping(BASE_URL + "/{id}")
    ResponseEntity<@NonNull DefaultResponse> one(@PathVariable UUID id) {
        return ResponseEntity.ok(new DefaultResponse("Company find successfully", service.findById(id), BASE_URL, id));
    }

    @Operation(
            summary = "Atualizar empresa",
            description = "Esta rota é utilizada pelo admin ou dono para atualizar dados da empresa."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empresa atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
    })
    @PutMapping(BASE_URL + "/{id}")
    ResponseEntity<@NonNull DefaultResponse> update(@RequestBody @Valid UpdateCompanyRequestDTO request, @PathVariable UUID id) {
        return ResponseEntity.ok(new DefaultResponse("Company successfully updated", service.update(id, request), BASE_URL, id));
    }
}
