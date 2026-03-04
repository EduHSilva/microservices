package com.edu.silva.notifications.repositories;

import com.edu.silva.notifications.domain.entities.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmailRepository extends JpaRepository<Email, UUID> {
}
