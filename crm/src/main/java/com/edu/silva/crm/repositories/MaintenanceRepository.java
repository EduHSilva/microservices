package com.edu.silva.crm.repositories;

import com.edu.silva.crm.domain.entities.Maintenance;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface MaintenanceRepository extends JpaRepository<@NonNull Maintenance, @NonNull UUID> {
}