package com.edu.silva.email.services.impl;

import com.edu.silva.email.domain.entities.Email;
import com.edu.silva.email.domain.enums.StatusEmail;
import com.edu.silva.email.repositories.EmailRepository;
import com.edu.silva.email.services.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailServiceImpl implements EmailService {
    final EmailRepository emailRepository;
    final JavaMailSender mailSender;

    public EmailServiceImpl(EmailRepository emailRepository, JavaMailSender mailSender) {
        this.emailRepository = emailRepository;
        this.mailSender = mailSender;
    }

    @Value(value = "${spring.mail.username}")
    private String emailFrom;

    @Transactional
    public void sendEmail(Email email) {
        try {
            email.setSentDate(LocalDateTime.now());
            email.setEmailFrom(emailFrom);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email.getEmailTo());
            message.setFrom(email.getEmailFrom());
            message.setText(email.getContent());
            message.setSubject(email.getSubject());

            mailSender.send(message);

            email.setStatus(StatusEmail.SENT);
        } catch (MailException ex) {
            email.setStatus(StatusEmail.ERROR);
        }
        emailRepository.save(email);
    }
}
