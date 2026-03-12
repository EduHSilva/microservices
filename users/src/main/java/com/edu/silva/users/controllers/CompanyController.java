package com.edu.silva.users.controllers;

import com.edu.silva.common.DefaultResponse;
import com.edu.silva.users.domain.dtos.requests.RegisterRequestDTO;
import com.edu.silva.users.domain.dtos.requests.UpdateCompanyRequestDTO;
import com.edu.silva.users.domain.dtos.requests.UpdateUserRequestDTO;
import com.edu.silva.users.domain.dtos.responses.CompanyResponseDTO;
import com.edu.silva.users.domain.dtos.responses.UserResponseDTO;
import com.edu.silva.users.domain.entities.User;
import com.edu.silva.users.services.CompanyService;
import com.edu.silva.users.services.UserService;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

@RestController()
public class CompanyController {
    private final CompanyService service;

    private final String BASE_URL = "/companies";

    public CompanyController(CompanyService service) {
        this.service = service;
    }

    @GetMapping(BASE_URL)
    ResponseEntity<@NonNull DefaultResponse> all(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<@NonNull CompanyResponseDTO> company = service.findAll(page, size);
        return ResponseEntity.ok(new DefaultResponse("Find all companies successfully", company, BASE_URL));
    }

    @GetMapping(BASE_URL + "/{id}")
    ResponseEntity<@NonNull DefaultResponse> one(@PathVariable UUID id) {
        return ResponseEntity.ok(new DefaultResponse("Company find successfully", service.findById(id), BASE_URL, id));
    }

    @PutMapping(BASE_URL + "/{id}")
    ResponseEntity<@NonNull DefaultResponse> update(@RequestBody @Valid UpdateCompanyRequestDTO request, @PathVariable UUID id) {
        return ResponseEntity.ok(new DefaultResponse("Company successfully updated", service.update(id, request), BASE_URL, id));
    }
}
