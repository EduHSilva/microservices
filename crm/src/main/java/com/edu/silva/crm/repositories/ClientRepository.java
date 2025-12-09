package com.edu.silva.crm.repositories;

import com.edu.silva.crm.domain.entities.Client;
import com.edu.silva.crm.domain.enums.ClientStatus;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface ClientRepository extends JpaRepository<@NonNull Client, @NonNull UUID> {
    Page<@NonNull Client> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<@NonNull Client> findByStatus(ClientStatus status, Pageable pageable);

    Page<@NonNull Client> findByNameContainingIgnoreCaseAndStatus(String name, ClientStatus status, Pageable pageable);

}