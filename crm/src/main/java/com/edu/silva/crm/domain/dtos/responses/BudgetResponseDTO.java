package com.edu.silva.crm.domain.dtos.responses;

import com.edu.silva.crm.domain.entities.Budget;
import com.edu.silva.crm.domain.entities.Client;
import com.edu.silva.crm.domain.enums.BudgetStatus;
import com.edu.silva.crm.domain.enums.ClientStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BudgetResponseDTO {
    private UUID id;
    private String title;
    private String description;
    private String terms;
    private BudgetStatus status;
    private String observations;
    private Date createdDate;

    public BudgetResponseDTO(Budget budget) {
        BeanUtils.copyProperties(budget, this);
    }
}
