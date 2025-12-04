package com.edu.silva.users.controllers;

import com.edu.silva.common.DefaultResponse;
import com.edu.silva.users.domain.dtos.requests.AuthRequestDTO;
import com.edu.silva.users.domain.dtos.responses.LoginResponseDTO;
import com.edu.silva.users.domain.entities.User;
import com.edu.silva.users.infra.security.TokenService;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @GetMapping("me")
    public ResponseEntity<?> me(@AuthenticationPrincipal User user) {
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(user.getId(), user.getUsername(), user.getEmail(), null);
        return ResponseEntity.ok(new DefaultResponse("Auth successfully verified", loginResponseDTO));
    }


    @PostMapping("login")
    public ResponseEntity<@NonNull DefaultResponse> login(@RequestBody @Valid AuthRequestDTO dto) {
        UsernamePasswordAuthenticationToken userNamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        Authentication authentication = authenticationManager.authenticate(userNamePassword);
        User user = (User) authentication.getPrincipal();
        if (user == null) return ResponseEntity.badRequest().build();
        String token = tokenService.generateToken(user);

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(user.getId(), user.getUsername(), user.getEmail(), token);
        return ResponseEntity.ok(new DefaultResponse("Auth successfully verified", loginResponseDTO));
    }
}
