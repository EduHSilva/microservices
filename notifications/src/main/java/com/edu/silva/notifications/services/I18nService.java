package com.edu.silva.notifications.services;

import java.util.Locale;

public interface I18nService {
    String getMessage(String key, Locale locale, Object... args);
}
