package com.eventer.admin.service;

import com.eventer.admin.domain.Event;
import com.eventer.admin.dto.event.CreateEventRequestDTO;
import com.eventer.admin.utils.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventService {
    Result<Event> create(CreateEventRequestDTO createEventDTO);

    Result<Page<Event>> getEvents(Pageable pageable);
}
