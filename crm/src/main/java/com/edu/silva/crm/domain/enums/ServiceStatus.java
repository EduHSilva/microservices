package com.edu.silva.crm.domain.enums;

import lombok.Getter;

@Getter
public enum ServiceStatus {
    NEW("new"),
    WORKING("working"),
    COMPLETED("completed"),
    DELETED("deleted");

    private final String status;

    ServiceStatus(String status) {
        this.status = status;
    }
}
