package com.edu.silva.users.domain.producers;

import com.edu.silva.common.dtos.EmailDTO;
import com.edu.silva.common.enums.EmailType;
import com.edu.silva.users.domain.entities.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserProducer {
    final RabbitTemplate rabbitTemplate;

    public UserProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value(value = "${broker.queue.email.name}")
    private String routingKey;

    @Value(value = "${server.url}")
    private String url;

    public void publishMessageEmail(User user) {
        EmailDTO emailDto = new EmailDTO();
        emailDto.setEmailTo(user.getEmail());
        emailDto.setUserID(user.getId());
        emailDto.setUsername(user.getUsername());
        emailDto.setBtnURL(url + "/user/confirm/" + user.getId());
        emailDto.setLocaleTag("pt-BR");
        emailDto.setEmailType(EmailType.NEW_USER);

        rabbitTemplate.convertAndSend("", routingKey, emailDto);
    }

}
