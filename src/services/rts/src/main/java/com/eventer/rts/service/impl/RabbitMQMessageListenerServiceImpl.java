package com.eventer.rts.service.impl;

import com.eventer.rts.contracts.ApplicationStatics;
import com.eventer.rts.service.MessageListenerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.Instant;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class RabbitMQMessageListenerServiceImpl implements MessageListenerService {
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @RabbitListener(queues = ApplicationStatics.RTS_MESSAGE_QUEUE)
    @Override
    public void receiveMessage(String message) {
        System.out.println("RECEIVED MESSAGE AT: " + Instant.now().toString());
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
