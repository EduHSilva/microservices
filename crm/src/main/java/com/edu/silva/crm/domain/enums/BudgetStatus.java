package com.edu.silva.crm.domain.enums;

import lombok.Getter;

@Getter
public enum BudgetStatus {
    NEW("new"),
    SEND("working"),
    APPROVED("approved"),
    DENY("deny"),
    DELETED("deleted");

    private final String status;

    BudgetStatus(String status) {
        this.status = status;
    }
}
