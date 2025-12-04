package com.edu.silva.crm.repositories;

import com.edu.silva.crm.domain.entities.Budget;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface BudgetRepository extends JpaRepository<@NonNull Budget, @NonNull UUID> {
}