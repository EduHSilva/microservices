package com.edu.silva.users.repositories;

import com.edu.silva.users.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;


public interface UserRepository extends JpaRepository<User, UUID> {
    UserDetails findByEmail(String email);
}