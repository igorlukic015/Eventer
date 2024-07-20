package com.eventer.user.service;

import com.eventer.user.cache.data.model.Event;
import com.github.igorlukic015.resulter.Result;

public interface SubscriptionService {
    void sendNewEventNotification(Event event);
    void sendEventUpdateNotification(Event event);
    Result create(String entityType, Long entityId);
    Result<Boolean> getIsEventSubscribed(Long eventId);
}
