package com.edu.silva.users.controllers;

import com.edu.silva.users.domain.dtos.requests.AuthRequestDTO;
import com.edu.silva.users.domain.dtos.responses.LoginResponseDTO;
import com.edu.silva.users.domain.entities.User;
import com.edu.silva.users.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<?> me() {
        return ResponseEntity.ok().build();
    }


    @PostMapping("login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthRequestDTO dto) {
        UsernamePasswordAuthenticationToken userNamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        Authentication authentication = authenticationManager.authenticate(userNamePassword);
        User user = (User) authentication.getPrincipal();
        String token = tokenService.generateToken(user);

        return ResponseEntity.ok(new LoginResponseDTO(user.getId(), user.getUsername(), user.getEmail(), token));
    }
}
