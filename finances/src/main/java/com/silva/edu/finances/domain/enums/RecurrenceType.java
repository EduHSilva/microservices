package com.silva.edu.finances.domain.enums;

public enum RecurrenceType {
    INSTALLMENT("installment"),
    MONTH("month");


    private final String type;

    RecurrenceType(String c) {
        this.type = c;
    }
}
