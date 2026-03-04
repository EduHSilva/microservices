package com.edu.silva.tasks.domain.producers;

import com.edu.silva.common.dtos.EmailDTO;
import com.edu.silva.tasks.domain.entities.Task;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TaskProducer {
    final RabbitTemplate rabbitTemplate;

    public TaskProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value(value = "${broker.queue.email.name}")
    private String routingKey;

    @Value(value = "${server.url}")
    private String url;

    public void publishMessageEmail(Task client) {
        EmailDTO emailDto = new EmailDTO();
//        emailDto.setEmailTo(client.getEmail());
//        emailDto.setUserID(client.getId());
//        emailDto.setUsername(client.getName());
        emailDto.setLocaleTag("pt-BR");
//        emailDto.setEmailType(EmailType.NEW_USER);

        rabbitTemplate.convertAndSend("", routingKey, emailDto);
    }

}
