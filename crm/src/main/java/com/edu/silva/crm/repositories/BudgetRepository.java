package com.edu.silva.crm.repositories;

import com.edu.silva.crm.domain.entities.Budget;
import com.edu.silva.crm.domain.enums.BudgetStatus;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface BudgetRepository extends JpaRepository<@NonNull Budget, @NonNull UUID> {
    Page<@NonNull Budget> findByTitleContainingIgnoreCase(String name, Pageable pageable);

    Page<@NonNull Budget> findByStatus(BudgetStatus status, Pageable pageable);

    Page<@NonNull Budget> findByTitleContainingIgnoreCaseAndStatus(String name, BudgetStatus status, Pageable pageable);

    List<Budget> findTop5ByOrderByCreatedDateDesc();

    List<Budget> findByStatus(BudgetStatus status);
}