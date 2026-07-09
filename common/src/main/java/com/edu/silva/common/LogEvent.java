package com.edu.silva.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class LogEvent {
    private Instant timestamp;
    private String userID;
    private String action;
    private String service;
    private String ip;
    private final Map<String, String> metadata;
}
