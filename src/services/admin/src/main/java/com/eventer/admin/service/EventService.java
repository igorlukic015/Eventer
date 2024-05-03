package com.eventer.admin.service;

import com.eventer.admin.contracts.event.UpdateEventRequest;
import com.eventer.admin.service.domain.Event;
import com.eventer.admin.contracts.event.CreateEventRequest;
import com.github.igorlukic015.resulter.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface EventService {
    Result<Event> create(CreateEventRequest createEventRequest);

    Result<Page<Event>> getEvents(Pageable pageable);

    Result<Set<Event>> getAllEvents();

    Result<Event> update(UpdateEventRequest request);

    Result delete(Long id);
}
