package com.edu.silva.crm.services;

import com.edu.silva.crm.domain.dtos.responses.DashboardKanbanResponseDTO;
import com.edu.silva.crm.domain.dtos.responses.DashboardResponseDTO;

public interface DashboardService {
    DashboardResponseDTO getData ();
    DashboardKanbanResponseDTO getDataKanban();
}
