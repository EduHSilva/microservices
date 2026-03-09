package com.edu.silva.users.services.impl;

import com.edu.silva.common.enums.Status;
import com.edu.silva.users.domain.dtos.requests.RegisterRequestDTO;
import com.edu.silva.users.domain.dtos.requests.UpdateUserRequestDTO;
import com.edu.silva.users.domain.dtos.responses.UserResponseDTO;
import com.edu.silva.users.domain.entities.Company;
import com.edu.silva.users.domain.entities.User;
import com.edu.silva.users.domain.enums.UserRole;
import com.edu.silva.users.domain.enums.UserStatus;
import com.edu.silva.users.domain.producers.UserProducer;
import com.edu.silva.users.infra.exceptions.CustomExceptions;
import com.edu.silva.users.repositories.CompanyRepository;
import com.edu.silva.users.repositories.UserRepository;
import com.edu.silva.users.services.UserService;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final CompanyRepository companyRepository;
    private final UserProducer producer;

    public UserServiceImpl(UserRepository userRepository, CompanyRepository companyRepository, UserProducer producer) {
        this.repository = userRepository;
        this.companyRepository = companyRepository;
        this.producer = producer;
    }

    @Transactional
    public UserResponseDTO save(RegisterRequestDTO dto) {
        if (repository.findByEmail(dto.email()) != null) {
            throw new CustomExceptions.EmailAlreadyExistsException(dto.email());
        }
        if (dto.role().equals(UserRole.ADMIN)) {
            throw new CustomExceptions.InvalidRoleException("User cannot register with admin role");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
        User user = new User(dto.username(), dto.email(), encryptedPassword, Collections.singletonList(dto.role()));
        Company company = new Company();
        if (dto.companyID() != null) {
            Optional<Company> optionalCompany = companyRepository.findById(dto.companyID());
            if (optionalCompany.isPresent()) {
                company = optionalCompany.get();
            } else {
                throw new CustomExceptions.EntityNotFoundException(Company.class.getSimpleName(), dto.companyID());
            }
        } else {
            company.setName(dto.username());
            company.setOwner(user);
            company.setStatus(Status.ACTIVE);
        }

        user.setStatus(UserStatus.WAITING_CONFIRMATION);
        user.setCompany(company);
        repository.save(user);
        companyRepository.save(company);
        producer.publishMessageEmail(user);
        return new UserResponseDTO(user);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Page<@NonNull UserResponseDTO> findAll(int page, int size, User user) {
        if (!user.getRoles().contains(UserRole.ADMIN)) {
            throw new CustomExceptions.InvalidRoleException("User does not have ADMIN role");
        }
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable).map(UserResponseDTO::new);
    }

    @Override
    public UserResponseDTO findById(UUID id) {
        return new UserResponseDTO(repository.findById(id)
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(User.class.getSimpleName(), id)));
    }

    @Override
    public UserResponseDTO update(UUID id, UpdateUserRequestDTO updateUserRequest) {
        return repository.findById(id)
                .map(user -> {
                    user.setUsername(updateUserRequest.username());
                    return new UserResponseDTO(repository.save(user));
                })
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(User.class.getSimpleName(), id));
    }

    @Override
    public UserResponseDTO confirm(UUID id) {
        return repository.findById(id)
                .map(user -> {
                    if (user.getStatus() == UserStatus.WAITING_CONFIRMATION) {
                        user.setStatus(UserStatus.OK);
                        return new UserResponseDTO(repository.save(user));
                    } else {
                        throw new CustomExceptions.InvalidStatusException("Invalid status for user confirmation");
                    }
                })
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(User.class.getSimpleName(), id));
    }
}
