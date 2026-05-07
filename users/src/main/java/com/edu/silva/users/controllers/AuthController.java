package com.edu.silva.users.controllers;

import com.edu.silva.common.DefaultResponse;
import com.edu.silva.users.domain.dtos.requests.AuthRequestDTO;
import com.edu.silva.users.domain.dtos.requests.RegisterRequestDTO;
import com.edu.silva.users.domain.dtos.responses.LoginResponseDTO;
import com.edu.silva.users.domain.dtos.responses.UserResponseDTO;
import com.edu.silva.users.domain.entities.User;
import com.edu.silva.users.services.UserService;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {
    private final String BASE_URL_USERS = "/users";

    private final UserService service;

    public AuthController(UserService service) {
        this.service = service;
    }

    @GetMapping("me")
    public ResponseEntity<?> me(@AuthenticationPrincipal User user) {
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(user.getId(), user.getUsername(), user.getEmail(), null);
        return ResponseEntity.ok(new DefaultResponse("Auth successfully verified", loginResponseDTO));
    }

    @PostMapping("register")
    ResponseEntity<@NonNull DefaultResponse> save(@RequestBody @Valid RegisterRequestDTO dto) {
        UserResponseDTO u = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new DefaultResponse("User successfully saved", u, BASE_URL_USERS, u.getId())
        );
    }

    @PostMapping("login")
    public ResponseEntity<DefaultResponse> login(@RequestBody @Valid AuthRequestDTO dto) {
        return ResponseEntity.ok(new DefaultResponse("Auth successfully verified", service.login(dto)));
    }
}
