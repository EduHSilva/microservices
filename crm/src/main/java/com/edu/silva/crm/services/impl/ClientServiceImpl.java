package com.edu.silva.crm.services.impl;

import com.edu.silva.crm.domain.dtos.requests.RegisterClientDTO;
import com.edu.silva.crm.domain.dtos.requests.UpdateClientDTO;
import com.edu.silva.crm.domain.entities.Client;
import com.edu.silva.crm.domain.enums.ClientStatus;
import com.edu.silva.crm.domain.producers.ClientProducer;
import com.edu.silva.crm.infra.exceptions.CustomExceptions;
import com.edu.silva.crm.repositories.ClientRepository;
import com.edu.silva.crm.services.ClientService;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;
    private final ClientProducer producer;

    public ClientServiceImpl(ClientRepository userRepository, ClientProducer producer) {
        this.repository = userRepository;
        this.producer = producer;
    }

    @Transactional
    public Client save(RegisterClientDTO dto) {
        if (repository.findByEmail(dto.email()) != null) {
            throw new CustomExceptions.EmailAlreadyExistsException(dto.email());
        }

        Client client = new Client(dto.name(), dto.email(), dto.phone(), dto.observations());
        client.setStatus(ClientStatus.NEW);
        repository.save(client);
        producer.publishMessageEmail(client);
        return client;
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public List<Client> findAll() {
        return List.of();
    }

    @Override
    public Client findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(Client.class.getName(), id));
    }

    @Override
    public Client update(UUID id, UpdateClientDTO dto) {
        return repository.findById(id)
                .map(user -> {
                    user.setObservations(dto.observations());
                    return repository.save(user);
                })
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(Client.class.getName(), id));
    }
}
