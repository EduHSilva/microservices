package com.edu.silva.crm.domain.enums;

import lombok.Getter;

@Getter
public enum ServicePriority {
    LOW("low"),
    MEDIUM("medium"),
    DONE("done");

    private final String priority;

    ServicePriority(String priority) {
        this.priority = priority;
    }
}
