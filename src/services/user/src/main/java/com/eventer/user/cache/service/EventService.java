package com.eventer.user.cache.service;

import com.eventer.user.cache.data.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventService {
    void reloadEvents();

    Page<Event> getEvents(final Pageable pageable);
}
