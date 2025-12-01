package com.edu.silva.email.services.impl;

import com.edu.silva.email.domain.entities.Email;
import com.edu.silva.email.domain.enums.StatusEmail;
import com.edu.silva.email.repositories.EmailRepository;
import com.edu.silva.email.services.EmailService;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    final EmailRepository emailRepository;
    final JavaMailSender mailSender;
    final Handlebars handlebars;
    final I18nServiceImpl i18n;

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

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(email.getEmailTo());
            helper.setSubject(email.getSubject());
            helper.setText(email.getContent(), true);

            mailSender.send(mimeMessage);

            email.setStatus(StatusEmail.SENT);
        } catch (Exception ex) {
            email.setStatus(StatusEmail.ERROR);
        }
        emailRepository.save(email);
    }

    @Override
    public String render(String templateName, Locale locale, Map<String, Object> vars) {
        try {
            Template template = handlebars.compile(templateName);
            Map<String, Object> ctx = new HashMap<>(vars);

            ctx.put("locale", locale.toLanguageTag());
            ctx.put("title", vars.getOrDefault(
                    "title",
                    ""
            ));
            ctx.put("footer", i18n.getMessage("footer", locale));

            return template.apply(ctx);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao renderizar template: " + templateName, e);
        }
    }
}
