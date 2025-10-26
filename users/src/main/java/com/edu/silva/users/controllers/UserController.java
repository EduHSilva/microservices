package com.edu.silva.users.controllers;

import com.edu.silva.users.domain.RegisterRequestDTO;
import com.edu.silva.users.domain.UserRole;
import com.edu.silva.users.domain.UserStatus;
import com.edu.silva.users.domain.entities.User;
import com.edu.silva.users.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserRepository repository;

    UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/users")
    List<User> all() {
        return repository.findAll();
    }

    @GetMapping("/user/{id}")
    User one(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping("/user")
    public ResponseEntity<User> newData(@RequestBody @Valid RegisterRequestDTO dto) {
        if (repository.findByEmail(dto.email()) != null) {
            return ResponseEntity.badRequest().build();
        }
        if (dto.role().equals(UserRole.ADMIN)) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
        User user = new User(dto.username(), dto.email(), encryptedPassword, dto.role());
        user.setStatus(UserStatus.WAITING_CONFIRMATION);
        repository.save(user);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/user/{id}")
    User replace(@RequestBody User newData, @PathVariable Long id) {
        return repository.findById(id)
                .map(user -> {
                    user.setUsername(newData.getUsername());
                    return repository.save(user);
                })
                .orElseGet(() -> repository.save(newData));
    }

    @DeleteMapping("/user/{id}")
    void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
