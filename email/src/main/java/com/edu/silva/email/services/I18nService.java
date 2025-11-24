package com.edu.silva.email.services;

import com.edu.silva.email.domain.entities.Email;

import java.util.Locale;
import java.util.Map;

public interface I18nService {
    String getMessage(String key, Locale locale, Object... args);
}
