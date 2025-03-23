package com.eventer.rts.web.v1;

import com.eventer.rts.service.MessageListenerService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.CopyOnWriteArrayList;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/rts")
public class SSEController {

    private final MessageListenerService messageListenerService;

    public SSEController(MessageListenerService messageListenerService) {
        this.messageListenerService = messageListenerService;
    }

    @GetMapping("/stream")
    public SseEmitter stream() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        this.messageListenerService.getEmitters().add(emitter);

        emitter.onCompletion(() -> this.messageListenerService.getEmitters().remove(emitter));
        emitter.onTimeout(() -> this.messageListenerService.getEmitters().remove(emitter));

        return emitter;
    }
}
