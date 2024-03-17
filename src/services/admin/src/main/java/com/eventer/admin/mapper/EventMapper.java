package com.eventer.admin.mapper;

import com.eventer.admin.contracts.event.CreateEventRequest;
import com.eventer.admin.service.domain.Event;
import com.eventer.admin.service.domain.EventCategory;
import com.eventer.admin.service.domain.Image;
import com.eventer.admin.service.domain.WeatherCondition;
import com.github.igorlukic015.resulter.Result;

import com.eventer.admin.web.dto.event.CreateEventDTO;
import com.eventer.admin.web.dto.event.EventDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.nio.file.Path;
import java.time.Instant;
import java.time.format.DateTimeParseException;
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
                        .collect(Collectors.toSet()),
                domain.getImages().stream().map(ImageMapper::toDTO).collect(Collectors.toSet()));
    }

    public static Set<EventDTO> toDTOs(Set<Event> domainSet) {
        return domainSet.stream().map(EventMapper::toDTO).collect(Collectors.toSet());
    }

    public static Page<EventDTO> toDTOPage(Page<Event> domainPage) {
        List<EventDTO> dtos = domainPage.stream().map(EventMapper::toDTO).toList();

        return new PageImpl<>(dtos, domainPage.getPageable(), domainPage.getTotalElements());
    }

    public static CreateEventRequest toRequest(CreateEventDTO dto, Set<Path> savedImages) {
        Result<Set<WeatherCondition>> conditionsOrError =
                Result.getResultValueSet(
                        dto.getWeatherConditions().stream()
                                .map(WeatherCondition::create)
                                .collect(Collectors.toSet()));

        Result<Set<EventCategory>> categoriesOrError =
                EventCategoryMapper.toDomainSet(dto.getEventCategories());

        Result<Instant> dateOrError;

        try {
            dateOrError = Result.success(Instant.parse(dto.getDate()));
        } catch (DateTimeParseException e) {
            dateOrError = Result.invalid(e.getMessage());
        }

        return new CreateEventRequest(
                dto.getTitle(),
                dto.getDescription(),
                dto.getLocation(),
                dateOrError,
                conditionsOrError,
                categoriesOrError,
                savedImages);
    }

    public static Result<Event> toDomain(com.eventer.admin.data.model.Event model) {
        Result<Set<EventCategory>> categoriesOrError =
                Result.getResultValueSet(
                        model.getCategories().stream()
                                .map(EventCategoryMapper::toDomain)
                                .collect(Collectors.toSet()));

        Result<Set<Image>> imagesOrError =
                ImageMapper.toDomainSet(model.getImages().stream().toList());

        return Event.create(
                model.getId(),
                model.getTitle(),
                model.getDescription(),
                model.getLocation(),
                model.getDate(),
                model.getWeatherConditionAvailability(),
                categoriesOrError,
                imagesOrError);
    }

    public static Result<Set<Event>> toDomainSet(
            List<com.eventer.admin.data.model.Event> foundEvents) {

        return Result.getResultValueSet(
                foundEvents.stream().map(EventMapper::toDomain).toList(), Collectors.toSet());
    }

    public static Result<Page<Event>> toDomainPage(
            Page<com.eventer.admin.data.model.Event> foundEvents) {
        Result<List<Event>> eventsOrError =
                Result.getResultValueSet(
                        foundEvents.stream().map(EventMapper::toDomain).toList(),
                        Collectors.toList());

        if (eventsOrError.isFailure()) {
            return Result.fromError(eventsOrError);
        }

        Page<Event> result =
                new PageImpl<>(
                        eventsOrError.getValue(),
                        foundEvents.getPageable(),
                        foundEvents.getTotalElements());

        return Result.success(result);
    }

    public static com.eventer.admin.data.model.Event toModel(Event event, Set<Image> images) {
        var model = new com.eventer.admin.data.model.Event();

        model.setId(event.getId());
        model.setTitle(event.getTitle());
        model.setDescription(event.getDescription());
        model.setLocation(event.getLocation());
        model.setDate(event.getDate());

        model.setWeatherConditionAvailability(
                String.join(
                        WeatherCondition.serializationSeparator,
                        event.getWeatherConditionAvailability().stream()
                                .map(WeatherCondition::getName)
                                .toList()));

        model.setCategories(
                event.getCategories().stream()
                        .map(EventCategoryMapper::toModel)
                        .collect(Collectors.toSet()));

        model.setImages(
                images.stream()
                        .map(image -> ImageMapper.toModel(image, Event.class.getSimpleName()))
                        .collect(Collectors.toSet()));

        return model;
    }
}
