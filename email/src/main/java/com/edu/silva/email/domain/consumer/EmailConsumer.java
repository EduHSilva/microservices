package com.edu.silva.email.domain.consumer;

import com.edu.silva.common.dtos.EmailDTO;
import com.edu.silva.common.enums.EmailType;
import com.edu.silva.email.domain.entities.Email;
import com.edu.silva.email.services.impl.EmailServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {
    EmailServiceImpl emailService;

    public EmailConsumer(EmailServiceImpl emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "${broker.queue.email.name}")
    public void listenEmailQueue(@Payload EmailDTO dto) {
        Email email = new Email();
        email.setUserID(dto.getUserID());
        email.setEmailTo(dto.getEmailTo());
        getSubjectAndContent(email, dto.getEmailType());

        emailService.sendEmail(email);
    }


    private void getSubjectAndContent(Email email, EmailType type) {
        switch (type) {
            case NEW_USER:
                email.setSubject("New User");
                email.setContent("New User");
                break;
            case ACCOUNT_VERIFICATION_OK:
                email.setSubject("Account Verification Successful");
                email.setContent("Account Verification Successful");
                break;
            default:
                throw new IllegalArgumentException("Invalid email type");
        }
    }
}
