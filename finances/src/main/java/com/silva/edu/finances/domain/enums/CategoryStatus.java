package com.silva.edu.finances.domain.enums;

public enum CategoryStatus {
    ACTIVE("active"),
    DELETED("deleted");

    private final String status;

    CategoryStatus(String status) {
        this.status = status;
    }
}
