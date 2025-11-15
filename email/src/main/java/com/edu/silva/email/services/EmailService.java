package com.edu.silva.email.services;

import com.edu.silva.email.domain.entities.Email;

public interface EmailService {
    void sendEmail(Email email);
}
