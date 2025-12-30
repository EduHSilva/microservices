package com.silva.edu.finances.domain.enums;

public enum TransactionStatus {
    OK("ok"),
    DELAY("delay"),
    DELETED("deleted");

    private final String status;

    TransactionStatus(String status) {
        this.status = status;
    }
}
