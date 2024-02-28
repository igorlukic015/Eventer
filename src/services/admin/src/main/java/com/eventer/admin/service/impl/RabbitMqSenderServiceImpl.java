package com.eventer.admin.service.impl;

import com.eventer.admin.contracts.Message;
import com.eventer.admin.service.MessageSenderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqSenderServiceImpl implements MessageSenderService {
    private final RabbitTemplate rabbitTemplate;

    public RabbitMqSenderServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendMessage(String recipient, String message) {
        this.rabbitTemplate.convertAndSend(recipient, message);
    }
}
