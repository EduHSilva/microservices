package com.edu.silva.tasks.domain.dtos.requests;

import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record AddTaskDTO(
        @NotNull String title,
        @NotNull Date dueDate
) {
}
