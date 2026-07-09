package com.edu.silva.common;

import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Map;

public class Logger {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Logger.class);

    public static void log(
            String userId,
            String method,
            String path,
            String service,
            Map<String, Object> meta,
            String ip
    ) {
        LogEvent event = new LogEvent(
                Instant.now(),
                userId,
                method,
                path,
                service,
                ip,
                meta
        );

        sendToAuditService(event);
    }

    private static void sendToAuditService(LogEvent event) {
        log.info("{}", event);
    }
}
