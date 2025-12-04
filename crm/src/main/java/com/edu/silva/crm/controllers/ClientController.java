package com.edu.silva.crm.controllers;

import com.edu.silva.common.DefaultResponse;
import com.edu.silva.crm.domain.dtos.requests.NewClientRequestDTO;
import com.edu.silva.crm.domain.dtos.requests.UpdateClientRequestDTO;
import com.edu.silva.crm.domain.dtos.responses.ClientResponseDTO;
import com.edu.silva.crm.services.ClientService;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class ClientController {

    private final String BASE_URL = "/crm/clients";

    private final ClientService service;

    public ClientController(ClientService clientService) {
        this.service = clientService;
    }

    @GetMapping("/clients")
    public ResponseEntity<@NonNull DefaultResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<@NonNull ClientResponseDTO> clients = service.findAll(page, size);

        return ResponseEntity.ok(
                new DefaultResponse("Find all clients successfully", clients, BASE_URL)
        );
    }

    @PostMapping("/clients")
    public ResponseEntity<@NonNull DefaultResponse> add(
            @RequestBody @Valid NewClientRequestDTO dto
    ) {
        ClientResponseDTO client = service.save(dto);

        return ResponseEntity.ok(
                new DefaultResponse("Client successfully saved", client, BASE_URL, client.getId())
        );
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<@NonNull DefaultResponse> get(
            @PathVariable UUID id
    ) {
        ClientResponseDTO dto = service.findById(id);
        return ResponseEntity.ok(
                new DefaultResponse("Client successfully retrieved", dto, BASE_URL, id)
        );
    }

    @DeleteMapping("/clients/{id}")
    public ResponseEntity<@NonNull DefaultResponse> delete(
            @PathVariable UUID id
    ) {
        service.delete(id);
        return ResponseEntity.ok(
                new DefaultResponse("Client successfully deleted")
        );
    }

    @PutMapping("/clients/{id}")
    public ResponseEntity<@NonNull DefaultResponse> update(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateClientRequestDTO dto
    ) {
        return ResponseEntity.ok(
                new DefaultResponse("Client successfully saved", service.update(id, dto), BASE_URL, id)
        );
    }
}
