package com.eventer.user.service;

import com.eventer.user.cache.data.model.Event;
import com.eventer.user.security.contracts.CustomUserDetails;
import com.github.igorlukic015.resulter.Result;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

public interface SubscriptionService {
    void sendNewEventNotification(Event event);
    void sendEventUpdateNotification(Event event);
    Result create(String entityType, Long entityId, CustomUserDetails userDetails);
    Result<Boolean> getIsEventSubscribed(Long eventId, CustomUserDetails userDetails);

    Result<Set<Long>> getSubscribedCategoryIds(CustomUserDetails userDetails);
}
