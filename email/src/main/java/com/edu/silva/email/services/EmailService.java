package com.edu.silva.email.services;

import com.edu.silva.email.domain.entities.Email;

import java.util.Locale;
import java.util.Map;

public interface EmailService {
    void sendEmail(Email email);
    String render(String templateName, Locale locale, Map<String, Object> vars);
}
