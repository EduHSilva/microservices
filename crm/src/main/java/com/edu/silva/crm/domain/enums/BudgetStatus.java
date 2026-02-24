package com.edu.silva.crm.domain.enums;

import lombok.Getter;

@Getter
public enum BudgetStatus {
    DRAFT("draft"),
    WAITING("waiting"),
    APPROVED("approved"),
    WORKING("working"),
    DONE("done"),
    CANCEL("cancel"),
    DENY("deny"),
    DELETED("deleted");

    private final String status;

    BudgetStatus(String status) {
        this.status = status;
    }
}
