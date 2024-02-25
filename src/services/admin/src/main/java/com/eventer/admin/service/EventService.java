package com.eventer.admin.service;

import com.eventer.admin.service.domain.Event;
import com.eventer.admin.contracts.event.CreateEventRequest;
import com.github.cigor99.resulter.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventService {
    Result<Event> create(CreateEventRequest createEventRequest);

    Result<Page<Event>> getEvents(Pageable pageable);
}
