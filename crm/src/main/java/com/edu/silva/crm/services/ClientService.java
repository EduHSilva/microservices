package com.edu.silva.crm.services;

import com.edu.silva.crm.domain.dtos.requests.RegisterClientDTO;
import com.edu.silva.crm.domain.dtos.requests.UpdateClientDTO;
import com.edu.silva.crm.domain.entities.Client;

import java.util.List;
import java.util.UUID;

public interface ClientService {
    Client save(RegisterClientDTO request);
    void delete(UUID id);
    List<Client> findAll();
    Client findById(UUID id);
    Client update(UUID id, UpdateClientDTO request);
}
