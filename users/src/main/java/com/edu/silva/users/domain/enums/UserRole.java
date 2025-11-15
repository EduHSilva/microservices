package com.edu.silva.users.domain.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("admin"),
    CRM("crm");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }
}
