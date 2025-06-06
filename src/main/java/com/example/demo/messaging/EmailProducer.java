package com.example.demo.messaging;

import com.example.demo.entity.Email;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailProducer {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${email.queue.name}")
    String queueName;

    public void sendEmail(Email email) {
        rabbitTemplate.convertAndSend("", queueName, email);
    }
}
