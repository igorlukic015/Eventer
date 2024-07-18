package com.eventer.user.service;

import com.eventer.user.cache.data.model.Event;

public interface SubscriptionService {
    void sendNewEventNotification(Event event);
    void sendEventUpdateNotification(Event event);
}
