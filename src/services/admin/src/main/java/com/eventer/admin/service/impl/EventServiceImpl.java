package com.eventer.admin.service.impl;

import com.eventer.admin.contracts.event.CreateEventRequest;
import com.eventer.admin.service.domain.Event;
import com.eventer.admin.mapper.EventMapper;
import com.eventer.admin.data.repository.EventRepository;
import com.eventer.admin.service.EventService;
import com.eventer.admin.utils.Result;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.stream.Collectors;

public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Result<Event> create(CreateEventRequest createEventRequest) {
        if (createEventRequest.weatherConditions().stream().anyMatch(Result::isFailure)) {
            return Result.fromError(
                    createEventRequest.weatherConditions().stream()
                            .filter(Result::isFailure)
                            .findFirst()
                            .get());
        }

        if (createEventRequest.eventCategories().stream().anyMatch(Result::isFailure)) {
            return Result.fromError(
                    createEventRequest.eventCategories().stream()
                            .filter(Result::isFailure)
                            .findFirst()
                            .get());
        }

        Result<Event> eventOrError =
                Event.create(
                        createEventRequest.title(),
                        createEventRequest.description(),
                        createEventRequest.location(),
                        createEventRequest.weatherConditions().stream()
                                .map(Result::getValue)
                                .collect(Collectors.toSet()),
                        createEventRequest.eventCategories().stream()
                                .map(Result::getValue)
                                .collect(Collectors.toSet()));

        if (eventOrError.isFailure()) {
            return Result.fromError(eventOrError);
        }

        com.eventer.admin.data.model.Event event =
                EventMapper.toModel(eventOrError.getValue(), "SYSTEM");

        com.eventer.admin.data.model.Event result = this.eventRepository.save(event);

        return EventMapper.toDomain(result);
    }

    @Override
    public Result<Page<Event>> getEvents(Pageable pageable) {
        Page<com.eventer.admin.data.model.Event> foundEvents =
                this.eventRepository.findAll(pageable);

        Result<Page<Event>> eventsOrError = EventMapper.toDomainPage(foundEvents);

        if (eventsOrError.isFailure()) {
            return Result.fromError(eventsOrError);
        }

        return eventsOrError;
    }
}
