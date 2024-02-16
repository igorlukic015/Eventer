package com.eventer.admin.service;

import com.eventer.admin.service.domain.Event;
import com.eventer.admin.contracts.event.CreateEventRequest;
import com.eventer.admin.utils.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface EventService {
    Result<Event> create(CreateEventRequest createEventRequest);

    Result<Page<Event>> getEvents(Pageable pageable);
}
