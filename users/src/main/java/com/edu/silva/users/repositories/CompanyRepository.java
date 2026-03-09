package com.edu.silva.users.repositories;

import com.edu.silva.users.domain.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface CompanyRepository extends JpaRepository<Company, UUID> {
}