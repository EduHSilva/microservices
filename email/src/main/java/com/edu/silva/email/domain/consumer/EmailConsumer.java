package com.edu.silva.email.domain.consumer;

import com.edu.silva.common.dtos.EmailDTO;
import com.edu.silva.email.domain.entities.Email;
import com.edu.silva.email.services.impl.EmailServiceImpl;
import com.edu.silva.email.services.impl.I18nServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailConsumer {

    private final EmailServiceImpl emailService;
    private final I18nServiceImpl i18n;

    @RabbitListener(queues = "${broker.queue.email.name}")
    public void listenEmailQueue(@Payload EmailDTO dto) {
        Email email = new Email();
        email.setUserID(dto.getUserID());
        email.setEmailTo(dto.getEmailTo());

        setSubjectAndContent(email, dto);

        emailService.sendEmail(email);
    }

    private void setSubjectAndContent(Email email, EmailDTO dto) {
        Locale locale = Locale.forLanguageTag(dto.getLocaleTag());

        String subject;
        String body;
        String btnLabel;

        if(dto.getEmailType() != null) {
            switch (dto.getEmailType()) {
                case NEW_USER:
                    subject = i18n.getMessage("welcome.subject", locale, dto.getUsername());
                    body = i18n.getMessage("welcome.body", locale, dto.getUsername());
                    btnLabel = i18n.getMessage("welcome.button", locale, dto.getUsername());
                    break;

                default:
                    throw new IllegalArgumentException("Invalid email type");
            }

            String html = emailService.render(
                    "email",
                    locale,
                    Map.of(
                            "body", body,
                            "title", subject,
                            "username", dto.getUsername(),
                            "buttonUrl", dto.getBtnURL(),
                            "buttonLabel", btnLabel
                    )
            );

            email.setSubject(subject);
            email.setContent(html);
        }
    }
}

