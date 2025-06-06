package com.example.demo.messaging;

import com.example.demo.entity.Email;
import com.example.demo.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {
    @Autowired
    EmailService emailService;

    @RabbitListener(queues = "${email.queue.name}")
    public void emailConsumer(Email email) {
        emailService.sendEmail(email);
    }
}
