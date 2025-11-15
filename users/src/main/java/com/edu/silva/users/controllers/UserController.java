package com.edu.silva.users.controllers;

import com.edu.silva.users.domain.entities.User;
import com.edu.silva.users.domain.dtos.requests.RegisterRequestDTO;
import com.edu.silva.users.domain.dtos.requests.UpdateUserRequestDTO;
import com.edu.silva.users.services.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController {
    private final UserServiceImpl service;

    UserController(UserServiceImpl userService) {
        this.service = userService;
    }

    @GetMapping("/users")
    ResponseEntity<List<User>> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/user/{id}")
    ResponseEntity<User> one(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping("/user")
    ResponseEntity<User> save(@RequestBody @Valid RegisterRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @PutMapping("/user/{id}")
    ResponseEntity<User> replace(@RequestBody @Valid UpdateUserRequestDTO request, @PathVariable UUID id) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/user/{id}")
    ResponseEntity<User> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
