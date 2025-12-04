package com.edu.silva.users.services.impl;

import com.edu.silva.users.domain.dtos.requests.RegisterRequestDTO;
import com.edu.silva.users.domain.dtos.requests.UpdateUserRequestDTO;
import com.edu.silva.users.domain.dtos.responses.UserResponseDTO;
import com.edu.silva.users.domain.entities.User;
import com.edu.silva.users.domain.enums.UserRole;
import com.edu.silva.users.domain.enums.UserStatus;
import com.edu.silva.users.domain.producers.UserProducer;
import com.edu.silva.users.infra.exceptions.CustomExceptions;
import com.edu.silva.users.repositories.UserRepository;
import com.edu.silva.users.services.UserService;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    public UserResponseDTO save(RegisterRequestDTO dto) {
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
        return new UserResponseDTO(user);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Page<@NonNull UserResponseDTO> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable).map(UserResponseDTO::new);
    }

    @Override
    public UserResponseDTO findById(UUID id) {
        return new UserResponseDTO(repository.findById(id)
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(User.class.getName(), id)));
    }

    @Override
    public UserResponseDTO update(UUID id, UpdateUserRequestDTO updateUserRequest) {
        return repository.findById(id)
                .map(user -> {
                    user.setUsername(updateUserRequest.username());
                    return new UserResponseDTO(repository.save(user));
                })
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(User.class.getName(), id));
    }

    @Override
    public UserResponseDTO confirm(UUID id) {
        return repository.findById(id)
                .map(user -> {
                    if (user.getStatus() == UserStatus.WAITING_CONFIRMATION) {
                        user.setStatus(UserStatus.OK);
                        return new UserResponseDTO(repository.save(user));
                    } else {
                        throw new CustomExceptions.InvalidStatusException("User com status inválido para confirmação");
                    }
                })
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(User.class.getName(), id));
    }
}
