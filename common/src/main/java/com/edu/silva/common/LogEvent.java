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
    private String method;
    private String path;
    private String service;
    private String ip;
    private final Map<String, Object> metadata;

    @Override
    public String toString() {
        return String.format(
                """
                        ===================== AUDIT =====================
                        Time    : %s
                        User    : %s
                        Service : %s
                        Method  : %s
                        Path    : %s
                        IP      : %s
                        Meta    : %s
                        ================================================
                        """,
                timestamp,
                userID == null ? "anonymous" : userID,
                service,
                method,
                path,
                ip,
                metadata
        );
    }
}
