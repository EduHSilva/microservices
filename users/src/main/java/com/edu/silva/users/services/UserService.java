package com.edu.silva.users.services;

import com.edu.silva.users.domain.dtos.requests.RegisterRequestDTO;
import com.edu.silva.users.domain.entities.User;
import com.edu.silva.users.domain.dtos.requests.UpdateUserRequestDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User save(RegisterRequestDTO request);
    void delete(UUID id);
    List<User> findAll();
    User findById(UUID id);
    User update(UUID id, UpdateUserRequestDTO request);
}
