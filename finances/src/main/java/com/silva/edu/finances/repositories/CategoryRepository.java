package com.silva.edu.finances.repositories;

import com.silva.edu.finances.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
