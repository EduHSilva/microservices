package com.edu.silva.common;

import java.time.Instant;
import java.util.Map;

public class Logger {
    public static void log(
            String userId,
            String action,
            String resource,
            Map<String, String> meta
    ) {
        LogEvent event = new LogEvent(
                Instant.now(),
                userId,
                action,
                resource,
                "",
                meta
        );

        sendToAuditService(event);
    }

    private static void sendToAuditService(LogEvent event) {
        // TODO: Implement Kafka producer logic here
    }
}
