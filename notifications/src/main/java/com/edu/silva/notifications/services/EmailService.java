package com.edu.silva.notifications.services;

import com.edu.silva.notifications.domain.entities.Email;

import java.util.Locale;
import java.util.Map;

public interface EmailService {
    void sendEmail(Email email);
    String render(String templateName, Locale locale, Map<String, Object> vars);
}
