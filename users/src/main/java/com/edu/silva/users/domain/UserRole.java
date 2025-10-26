package com.edu.silva.users.domain;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("admin");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }
}
