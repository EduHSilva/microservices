package com.edu.silva.crm.repositories;

import com.edu.silva.crm.domain.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface ClientRepository extends JpaRepository<Client, UUID> {
    Client findByEmail(String email);
}