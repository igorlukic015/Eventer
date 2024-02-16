package com.eventer.admin.service.impl;

import com.eventer.admin.contracts.event.CreateEventRequest;
import com.eventer.admin.service.EventCategoryService;
import com.eventer.admin.service.domain.Event;
import com.eventer.admin.mapper.EventMapper;
import com.eventer.admin.data.repository.EventRepository;
import com.eventer.admin.service.EventService;
import com.eventer.admin.service.domain.EventCategory;
import com.eventer.admin.utils.Result;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventCategoryService eventCategoryService;

    public EventServiceImpl(
            EventRepository eventRepository, EventCategoryService eventCategoryService) {
        this.eventRepository = eventRepository;
        this.eventCategoryService = eventCategoryService;
    }

    @Override
    public Result<Event> create(CreateEventRequest createEventRequest) {
        if (createEventRequest.weatherConditionsOrError().isFailure()) {
            return Result.fromError(createEventRequest.weatherConditionsOrError());
        }

        if (createEventRequest.eventCategoriesOrError().isFailure()) {
            return Result.fromError(createEventRequest.eventCategoriesOrError());
        }

        Result<Set<EventCategory>> categoriesOrError =
                this.eventCategoryService.getCategoriesByIds(
                        createEventRequest.eventCategoriesOrError().getValue().stream()
                                .map(EventCategory::getId)
                                .collect(Collectors.toSet()));

        if (categoriesOrError.isFailure()) {
            return Result.fromError(categoriesOrError);
        }

        Result<Event> eventOrError =
                Event.create(
                        createEventRequest.title(),
                        createEventRequest.description(),
                        createEventRequest.location(),
                        createEventRequest.weatherConditionsOrError().getValue(),
                        createEventRequest.eventCategoriesOrError().getValue());

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
