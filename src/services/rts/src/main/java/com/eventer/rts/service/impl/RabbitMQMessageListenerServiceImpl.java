package com.eventer.rts.service.impl;

import com.eventer.rts.contracts.ApplicationStatics;
import com.eventer.rts.contracts.Message;
import com.eventer.rts.service.MessageListenerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class RabbitMQMessageListenerServiceImpl implements MessageListenerService {
//    private final ObjectMapper objectMapper;

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

//    public RabbitMQMessageListenerServiceImpl(ObjectMapper objectMapper) {
//        this.objectMapper = objectMapper;
//    }

    @RabbitListener(queues = ApplicationStatics.EVENTER_DATA_MESSAGE_QUEUE)
    @Override
    public void receiveMessage(String message) {
//        Message parsedMessage;
//        try {
//            parsedMessage = this.objectMapper.readValue(message, Message.class);
//
//        } catch (IllegalArgumentException | JsonProcessingException e) {
//            // TODO: All logging
//            return;
//        }

//        System.out.println("Received: " + parsedMessage.getName());
//
//        System.out.println(parsedMessage.getData());

        this.pushToClient(message);
    }

    @Override
    public CopyOnWriteArrayList<SseEmitter> getEmitters() {
        return emitters;
    }

    private void pushToClient(String message) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("message").data(message));
            } catch (Exception e) {
                emitters.remove(emitter);
            }
        }
    }
}
