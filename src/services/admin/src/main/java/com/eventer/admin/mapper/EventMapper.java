package com.eventer.admin.mapper;

import com.eventer.admin.domain.Event;
import com.eventer.admin.domain.EventCategory;
import com.eventer.admin.utils.Result;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class EventMapper {

    public static Result<Event> toDomain(com.eventer.admin.model.Event model) {
        List<Result<EventCategory>> categoriesOrError =
                model.getCategories().stream().map(EventCategoryMapper::toDomain).toList();

        if (categoriesOrError.stream().anyMatch(Result::isFailure)) {
            return Result.fromError(
                    categoriesOrError.stream().filter(Result::isFailure).findFirst().get());
        }

        Result<Event> eventOrError =
                Event.create(
                        model.getId(),
                        model.getTitle(),
                        model.getDescription(),
                        model.getLocation(),
                        model.getWeatherConditionAvailability(),
                        categoriesOrError.stream()
                                .map(Result::getValue)
                                .collect(Collectors.toSet()));

        if (eventOrError.isFailure()) {
            return Result.fromError(eventOrError);
        }

        return eventOrError;
    }

    public static Result<Page<Event>> toDomainPage(
            Page<com.eventer.admin.model.Event> foundEvents) {
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

    public static com.eventer.admin.model.Event toModel(Event event, String createdBy) {
        var model = new com.eventer.admin.model.Event();

        model.setId(event.getId());
        model.setTitle(event.getTitle());
        model.setDescription(event.getDescription());
        model.setLocation(event.getLocation());
        model.setCreatedBy(createdBy);

        return model;
    }
}
