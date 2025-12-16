package com.silva.edu.finances.repositories;

import com.silva.edu.finances.domain.entities.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionCategoryRepository extends JpaRepository<TransactionCategory, UUID> {
}
