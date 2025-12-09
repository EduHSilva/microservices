package com.edu.silva.crm.services.impl;

import com.edu.silva.crm.domain.dtos.requests.NewClientRequestDTO;
import com.edu.silva.crm.domain.dtos.requests.UpdateClientRequestDTO;
import com.edu.silva.crm.domain.dtos.responses.ClientResponseDTO;
import com.edu.silva.crm.domain.entities.Client;
import com.edu.silva.crm.domain.enums.ClientStatus;
import com.edu.silva.crm.infra.config.UserContext;
import com.edu.silva.crm.infra.exceptions.CustomExceptions;
import com.edu.silva.crm.repositories.ClientRepository;
import com.edu.silva.crm.services.ClientService;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;

    public ClientServiceImpl(ClientRepository userRepository) {
        this.repository = userRepository;
    }

    @Transactional
    public ClientResponseDTO save(NewClientRequestDTO dto) {
        Client client = new Client(dto.name(), dto.email(), dto.phone(), dto.observations());
        client.setStatus(ClientStatus.NEW);
        UUID userId = UserContext.getUser();
        client.setUserId(userId);
        repository.save(client);
        return new ClientResponseDTO(client);
    }

    @Override
    public void delete(UUID id) {
        repository.findById(id).ifPresent(entity -> repository.deleteById(id));
    }

    @Override
    public Page<@NonNull ClientResponseDTO> findAll(int page, int size, String name, String status) {
        Pageable pageable = PageRequest.of(page, size);
        if (name != null && status != null) {
            return repository
                    .findByNameContainingIgnoreCaseAndStatus(name, ClientStatus.valueOf(status.toUpperCase()), pageable)
                    .map(ClientResponseDTO::new);
        }

        if (name != null) {
            return repository
                    .findByNameContainingIgnoreCase(name, pageable)
                    .map(ClientResponseDTO::new);
        }

        if (status != null) {
            return repository
                    .findByStatus(ClientStatus.valueOf(status.toUpperCase()), pageable)
                    .map(ClientResponseDTO::new);
        }

        return repository.findAll(pageable).map(ClientResponseDTO::new);
    }

    @Override
    public ClientResponseDTO findById(UUID id) {
        return new ClientResponseDTO(repository.findById(id)
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(Client.class.getSimpleName(), id)));
    }

    @Override
    public ClientResponseDTO update(UUID id, UpdateClientRequestDTO dto) {
        return new ClientResponseDTO(repository.findById(id)
                .map(user -> {
                    user.setObservations(dto.observations());
                    return repository.save(user);
                })
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(Client.class.getSimpleName(), id)));
    }
}
