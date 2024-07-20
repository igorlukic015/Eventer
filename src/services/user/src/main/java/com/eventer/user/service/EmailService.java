package com.eventer.user.service;

import com.eventer.user.cache.data.model.Event;

import java.util.Set;

public interface EmailService {
    void sendNewEventNotification(Set<String> recipients, Event event);

    void sendEventUpdateNotification(Set<String> recipientEmails, Event event);
}
