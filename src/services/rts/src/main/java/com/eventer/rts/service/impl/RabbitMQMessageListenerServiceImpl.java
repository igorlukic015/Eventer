package com.eventer.rts.service.impl;

import com.eventer.rts.contracts.ApplicationStatics;
import com.eventer.rts.service.MessageListenerService;
import com.eventer.rts.web.v1.SSEController;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQMessageListenerServiceImpl implements MessageListenerService {
    private final SSEController sseController;

    public RabbitMQMessageListenerServiceImpl(SSEController sseController) {
        this.sseController = sseController;
    }

    @RabbitListener(queues = ApplicationStatics.EVENTER_DATA_MESSAGE_QUEUE)
    @Override
    public void receiveMessage(String message) {
        System.out.println("Received: " + message);

        this.sseController.pushToClient(message);
    }
}
