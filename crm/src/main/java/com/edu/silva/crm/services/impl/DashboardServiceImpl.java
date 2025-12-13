package com.edu.silva.crm.services.impl;

import com.edu.silva.crm.domain.dtos.responses.BudgetResponseDTO;
import com.edu.silva.crm.domain.dtos.responses.DashboardResponseDTO;
import com.edu.silva.crm.repositories.BudgetRepository;
import com.edu.silva.crm.services.DashboardService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final JdbcTemplate jdbc;
    private final BudgetRepository repository;

    public DashboardServiceImpl(JdbcTemplate jdbc, BudgetRepository repository) {
        this.jdbc = jdbc;
        this.repository = repository;
    }

    @Override
    public DashboardResponseDTO getData() {

        String sql = """
                  SELECT
                  COUNT(DISTINCT(c.id)) AS totalClients,
                  COUNT(b.id) FILTER (WHERE b.status = 'APPROVED') AS pendingServices,
                  COUNT(b.id) FILTER (WHERE b.status = 'WORKING') AS workingServices,
                  SUM(i.price_un * i.quantity) FILTER (WHERE b.status = 'DONE') AS totalRecieved
                FROM budgets b
                RIGHT JOIN clients c ON b.client_id = c.id
                LEFT JOIN item i ON i.budget_id = b.id;
                """;

        DashboardResponseDTO dto = jdbc.query(sql, (rs, rowNum) ->
                new DashboardResponseDTO(
                        rs.getInt("totalClients"),
                        rs.getInt("pendingServices"),
                        rs.getInt("workingServices"),
                        rs.getDouble("totalRecieved")
                )
        ).getFirst();


        dto.setRecentServices(repository.findTop5ByOrderByCreatedDateDesc().stream().map(BudgetResponseDTO::new).toList());

        return dto;
    }

}
