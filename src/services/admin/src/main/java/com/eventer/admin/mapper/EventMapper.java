package com.eventer.admin.mapper;

import com.eventer.admin.contracts.event.CreateEventRequest;
import com.eventer.admin.service.domain.Event;
import com.eventer.admin.service.domain.EventCategory;
import com.eventer.admin.service.domain.WeatherCondition;
import com.eventer.admin.utils.Result;

import com.eventer.admin.web.dto.event.CreateEventDTO;
import com.eventer.admin.web.dto.event.EventDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EventMapper {

    public static EventDTO toDTO(Event domain) {
        return new EventDTO(
                domain.getId(),
                domain.getTitle(),
                domain.getDescription(),
                domain.getLocation(),
                domain.getWeatherConditionAvailability().stream()
                        .map(WeatherCondition::getName)
                        .collect(Collectors.toSet()),
                domain.getCategories().stream()
                        .map(EventCategoryMapper::toDTO)
                        .collect(Collectors.toSet()));
    }

    public static Page<EventDTO> toDTOs(Page<Event> domainPage) {
        List<EventDTO> dtos = domainPage.stream().map(EventMapper::toDTO).toList();

        return new PageImpl<>(dtos, domainPage.getPageable(), domainPage.getTotalElements());
    }

    public static CreateEventRequest toRequest(CreateEventDTO dto) {
        Result<Set<WeatherCondition>> conditionsOrError =
                Result.getResultValueSet(
                        dto.weatherConditions().stream()
                                .map(WeatherCondition::create)
                                .collect(Collectors.toSet()));

        Result<Set<EventCategory>> categoriesOrError =
                Result.getResultValueSet(
                        dto.eventCategories().stream()
                                .map(EventCategoryMapper::toDomain)
                                .collect(Collectors.toSet()));

        return new CreateEventRequest(
                dto.title(),
                dto.description(),
                dto.location(),
                conditionsOrError,
                categoriesOrError);
    }

    public static Result<Event> toDomain(com.eventer.admin.data.model.Event model) {
        Result<Event> eventOrError =
                Event.create(
                        model.getId(),
                        model.getTitle(),
                        model.getDescription(),
                        model.getLocation(),
                        model.getWeatherConditionAvailability(),
                        model.getCategories().stream()
                                .map(EventCategoryMapper::toDomain)
                                .collect(Collectors.toSet()));

        if (eventOrError.isFailure()) {
            return Result.fromError(eventOrError);
        }

        return eventOrError;
    }

    public static Result<Page<Event>> toDomainPage(
            Page<com.eventer.admin.data.model.Event> foundEvents) {
        List<Result<Event>> events = foundEvents.stream().map(EventMapper::toDomain).toList();

        if (events.stream().anyMatch(Result::isFailure)) {
            return Result.fromError(events.stream().filter(Result::isFailure).findFirst().get());
        }

        Page<Event> result =
                new PageImpl<>(
                        events.stream().map(Result::getValue).toList(),
                        foundEvents.getPageable(),
                        foundEvents.getTotalElements());

        return Result.success(result);
    }

    public static com.eventer.admin.data.model.Event toModel(Event event, String createdBy) {
        var model = new com.eventer.admin.data.model.Event();

        model.setId(event.getId());
        model.setTitle(event.getTitle());
        model.setDescription(event.getDescription());
        model.setLocation(event.getLocation());

        model.setWeatherConditionAvailability(
                String.join(
                        ";",
                        event.getWeatherConditionAvailability().stream()
                                .map(WeatherCondition::getName)
                                .toList()));

        model.setCategories(
                event.getCategories().stream()
                        .map(EventCategoryMapper::toModel)
                        .collect(Collectors.toSet()));

        model.setCreatedBy(createdBy);

        return model;
    }
}
