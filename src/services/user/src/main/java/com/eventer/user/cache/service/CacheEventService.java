package com.eventer.user.cache.service;

import com.eventer.user.cache.data.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface CacheEventService {
    Set<Event> getAll();

    void add(Event newEvent);

    void update(Event updatedEvent);

    void reloadEvents();

    Page<Event> getEvents(final Pageable pageable);

    void remove(Long deletedId);
}
