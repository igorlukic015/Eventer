package com.eventer.admin.service.impl;

import com.eventer.admin.domain.Event;
import com.eventer.admin.dto.event.CreateEventRequestDTO;
import com.eventer.admin.mapper.EventMapper;
import com.eventer.admin.repository.EventRepository;
import com.eventer.admin.service.EventService;
import com.eventer.admin.utils.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Result<Event> create(CreateEventRequestDTO request) {
        Result<Event> eventOrError = Event.create(request.title(), request.description(), request.location(),
                request.weatherConditions(), request.eventCategories());

        if (eventOrError.isFailure()) {
            return Result.fromError(eventOrError);
        }

        com.eventer.admin.model.Event event = EventMapper.toModel(eventOrError.getValue(), "SYSTEM");

        com.eventer.admin.model.Event result = this.eventRepository.save(event);

        return EventMapper.toDomain(result);
    }

    @Override
    public Result<Page<Event>> getEvents(Pageable pageable) {
        Page<com.eventer.admin.model.Event> foundEvents = this.eventRepository.findAll(pageable);

        Result<Page<Event>> eventsOrError = EventMapper.toDomainPage(foundEvents);

        if (eventsOrError.isFailure()) {
            return Result.fromError(eventsOrError);
        }

        return eventsOrError;
    }
}
