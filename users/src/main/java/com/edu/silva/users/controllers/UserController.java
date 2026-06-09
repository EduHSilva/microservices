package com.edu.silva.users.controllers;

import com.edu.silva.common.DefaultResponse;
import com.edu.silva.users.domain.dtos.requests.UpdateUserRequestDTO;
import com.edu.silva.users.domain.dtos.responses.UserResponseDTO;
import com.edu.silva.users.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

@Tag(name = "Usuário")
@RestController
public class UserController {
    private final UserService service;

    private final String BASE_URL = "/users";

    public UserController(UserService userService) {
        this.service = userService;
    }

    @Operation(
            summary = "Buscar todos usuários",
            description = "Esta rota é utilizada pelo admin ou dono para buscar todas usuários."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuários recuperados com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não possui permissão")
    })
    @GetMapping()
    ResponseEntity<@NonNull DefaultResponse> all(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<@NonNull UserResponseDTO> users = service.findAll(page, size);
        return ResponseEntity.ok(new DefaultResponse("Find all users successfully", users, BASE_URL));
    }

    @Operation(
            summary = "Buscar usuário por ID",
            description = "Esta rota é utilizada para buscar detalhes de um usuário."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário recuperado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    ResponseEntity<@NonNull DefaultResponse> one(@PathVariable UUID id) {
        return ResponseEntity.ok(new DefaultResponse("User find successfully", service.findById(id), BASE_URL, id));
    }

    @Operation(
            summary = "Confirmar cadastro de usuário",
            description = "Esta rota é utilizada para confirmar cadastro do usuário."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário confirmado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "400", description = "Status invalido para confirmação")
    })
    @GetMapping(value = "/confirm/{id}")
    public RedirectView confirm(@PathVariable UUID id) {
        service.confirm(id);
        return new RedirectView("/confirm.html");
    }

    @Operation(
            summary = "Atualizar cadastro de usuário",
            description = "Esta rota é utilizada para um usuário atualizar seu proprio cadastro."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos")
    })
    @PutMapping("/{id}")
    ResponseEntity<@NonNull DefaultResponse> update(@RequestBody @Valid UpdateUserRequestDTO request, @PathVariable UUID id) {
        return ResponseEntity.ok(new DefaultResponse("User successfully updated", service.update(id, request), BASE_URL, id));
    }

    @Operation(
            summary = "Deletar cadastro de usuário",
            description = "Esta rota é utilizada para um usuário deletar seu proprio cadastro."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
    })
    @DeleteMapping("/{id}")
    ResponseEntity<@NonNull DefaultResponse> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok(new DefaultResponse("User successfully deleted"));
    }
}
