package com.edu.silva.email.services;

import java.util.Locale;

public interface I18nService {
    String getMessage(String key, Locale locale, Object... args);
}
