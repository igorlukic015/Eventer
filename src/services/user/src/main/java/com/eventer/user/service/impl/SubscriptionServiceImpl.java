package com.eventer.user.service.impl;

import com.eventer.user.cache.data.model.Event;
import com.eventer.user.cache.web.dto.EventCategoryDTO;
import com.eventer.user.data.model.CategorySubscription;
import com.eventer.user.data.model.EventSubscription;
import com.eventer.user.data.repository.CategorySubscriptionRepository;
import com.eventer.user.data.repository.EventSubscriptionRepository;
import com.eventer.user.service.SubscriptionService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final EventSubscriptionRepository eventSubscriptionRepository;
    private final CategorySubscriptionRepository categorySubscriptionRepository;

    public SubscriptionServiceImpl(
            EventSubscriptionRepository eventSubscriptionRepository,
            CategorySubscriptionRepository categorySubscriptionRepository) {
        this.eventSubscriptionRepository = eventSubscriptionRepository;
        this.categorySubscriptionRepository = categorySubscriptionRepository;
    }

    public void sendNewEventNotification(Event event) {
        Set<CategorySubscription> subscriptions =
                this.categorySubscriptionRepository.findAllByCategoryId(
                        event.getCategories().stream()
                                .map(EventCategoryDTO::id)
                                .collect(Collectors.toSet()));

        Set<String> recipientEmails =
                subscriptions.stream()
                        .map(s -> s.getUser().getUsername())
                        .collect(Collectors.toSet());
    }

    public void sendEventUpdateNotification(Event event) {
        Set<EventSubscription> subscriptions =
                this.eventSubscriptionRepository.findAllByEventId(event.getEventId());

        Set<String> recipientEmails =
                subscriptions.stream()
                        .map(s -> s.getUser().getUsername())
                        .collect(Collectors.toSet());
    }
}
