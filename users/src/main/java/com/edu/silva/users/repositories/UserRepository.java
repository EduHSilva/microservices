package com.edu.silva.users.repositories;

import com.edu.silva.users.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;


public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByEmail(String email);
}