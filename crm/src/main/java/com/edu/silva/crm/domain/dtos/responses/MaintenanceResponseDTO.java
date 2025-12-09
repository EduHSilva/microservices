package com.edu.silva.crm.domain.dtos.responses;

import com.edu.silva.crm.domain.enums.BudgetStatus;
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
public class MaintenanceResponseDTO {
    private UUID id;
    private String title;
    private String description;
    private String terms;
    private BudgetStatus status;
    private String observations;
    private Date createdDate;

    public MaintenanceResponseDTO(Maintenance maintenance) {
        BeanUtils.copyProperties(maintenance, this);
    }
}
