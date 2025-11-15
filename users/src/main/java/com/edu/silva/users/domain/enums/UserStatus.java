package com.edu.silva.users.domain.enums;

import lombok.Getter;

@Getter
public enum UserStatus {
    OK("ok"),
    WAITING_CONFIRMATION("waiting_confirmation"),
    DELETED("deleted"),
    BLOCKED("blocked");

    private final String status;

    UserStatus(String status) {
        this.status = status;
    }
}
