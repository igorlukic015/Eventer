package com.eventer.rts.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.CopyOnWriteArrayList;

public interface MessageListenerService {
    void receiveMessage(String message);

    CopyOnWriteArrayList<SseEmitter> getEmitters();
}
