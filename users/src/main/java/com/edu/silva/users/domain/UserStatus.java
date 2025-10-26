package com.edu.silva.users.domain;

import lombok.Getter;

@Getter
public enum UserStatus {
    OK("ok"),
    WAITING_CONFIRMATION("waiting_confirmation"),
    BLOCKED("blocked");

    private final String status;

    UserStatus(String status) {
        this.status = status;
    }


}
