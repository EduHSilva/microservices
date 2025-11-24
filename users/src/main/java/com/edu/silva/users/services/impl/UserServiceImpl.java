package com.edu.silva.users.services.impl;

import com.edu.silva.users.domain.dtos.requests.RegisterRequestDTO;
import com.edu.silva.users.domain.enums.UserRole;
import com.edu.silva.users.domain.enums.UserStatus;
import com.edu.silva.users.domain.entities.User;
import com.edu.silva.users.domain.dtos.requests.UpdateUserRequestDTO;
import com.edu.silva.users.infra.exceptions.CustomExceptions;
import com.edu.silva.users.domain.producers.UserProducer;
import com.edu.silva.users.repositories.UserRepository;
import com.edu.silva.users.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserProducer producer;

    public UserServiceImpl(UserRepository userRepository, UserProducer producer) {
        this.repository = userRepository;
        this.producer = producer;
    }

    @Transactional
    public User save(RegisterRequestDTO dto) {
        if (repository.findByEmail(dto.email()) != null) {
            throw new CustomExceptions.EmailAlreadyExistsException(dto.email());
        }
        if (dto.role().equals(UserRole.ADMIN)) {
            throw new CustomExceptions.InvalidRoleException("Usuários não podem se registrar como ADMIN.");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
        User user = new User(dto.username(), dto.email(), encryptedPassword, dto.role());
        user.setStatus(UserStatus.WAITING_CONFIRMATION);
        repository.save(user);
        producer.publishMessageEmail(user);
        return user;
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public User findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(User.class.getName(), id));
    }

    @Override
    public User update(UUID id, UpdateUserRequestDTO updateUserRequest) {
        return repository.findById(id)
                .map(user -> {
                    user.setUsername(updateUserRequest.username());
                    return repository.save(user);
                })
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(User.class.getName(), id));
    }

    @Override
    public User confirm(UUID id) {
        return repository.findById(id)
                .map(user -> {
                    if (user.getStatus() == UserStatus.WAITING_CONFIRMATION) {
                        user.setStatus(UserStatus.OK);
                        return repository.save(user);
                    } else {
                        throw new CustomExceptions.InvalidStatusException("User com status inválido para confirmação");
                    }
                })
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(User.class.getName(), id));
    }
}
