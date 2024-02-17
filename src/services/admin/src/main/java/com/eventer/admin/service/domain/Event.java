package com.eventer.admin.service.domain;

import com.eventer.admin.utils.Helpers;
import com.eventer.admin.utils.Result;
import com.eventer.admin.utils.ResultErrorMessages;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Event {
    private final Long id;

    private final String title;

    private final String description;

    private final String location;

    private final Instant date;

    private final Set<WeatherCondition> weatherConditionAvailability;

    private final Set<EventCategory> categories;

    private final Set<Image> images;

    private Event(
            Long id,
            String title,
            String description,
            String location,
            Instant date,
            Set<WeatherCondition> weatherConditionAvailability,
            Set<EventCategory> categories,
            Set<Image> images) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.date = date;
        this.weatherConditionAvailability = weatherConditionAvailability;
        this.categories = categories;
        this.images = images;
    }

    public static Result<Event> create(
            Long id,
            String title,
            String description,
            String location,
            Instant date,
            String weatherConditionAvailability,
            Result<Set<EventCategory>> categoriesOrError,
            Result<Set<Image>> imagesOrError) {
        if (categoriesOrError.isFailure()) {
            return Result.fromError(categoriesOrError);
        }

        if (imagesOrError.isFailure()) {
            return Result.fromError(imagesOrError);
        }

        Result<Set<WeatherCondition>> weatherConditionsOrError =
                Result.getResultValueSet(
                        Arrays.stream(
                                        weatherConditionAvailability.split(
                                                WeatherCondition.serializationSeparator))
                                .map(WeatherCondition::create)
                                .collect(Collectors.toSet()));

        if (weatherConditionsOrError.isFailure()) {
            return Result.fromError(weatherConditionsOrError);
        }

        Event event =
                new Event(
                        id,
                        title,
                        description,
                        location,
                        date,
                        weatherConditionsOrError.getValue(),
                        categoriesOrError.getValue(),
                        imagesOrError.getValue());

        return Result.success(event);
    }

    public static Result<Event> create(
            String title,
            String description,
            String location,
            Instant date,
            Set<WeatherCondition> weatherConditions,
            Set<EventCategory> categories,
            Set<Image> images) {

        if (Helpers.isNullOrEmpty(title) || title.length() > 255) {
            return Result.invalid(ResultErrorMessages.invalidEventTitle);
        }

        if (description.length() > 255) {
            return Result.invalid(ResultErrorMessages.invalidEventDescription);
        }

        if (Helpers.isNullOrEmpty(location) || location.length() > 255) {
            return Result.invalid(ResultErrorMessages.invalidEventLocation);
        }

        Event event =
                new Event(
                        null,
                        title,
                        description,
                        location,
                        date,
                        weatherConditions,
                        categories,
                        images);

        return Result.success(event);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public Instant getDate() {
        return date;
    }

    public Set<WeatherCondition> getWeatherConditionAvailability() {
        return weatherConditionAvailability;
    }

    public Set<EventCategory> getCategories() {
        return categories;
    }
}
