package com.eventer.admin.service.domain;

import com.eventer.admin.utils.Helpers;
import com.eventer.admin.utils.Result;
import com.eventer.admin.utils.ResultErrorMessages;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Event {
    private final Long id;

    private final String title;

    private final String description;

    private final String location;

    private final Set<WeatherCondition> weatherConditionAvailability;

    private final Set<EventCategory> categories;

    private Event(
            Long id,
            String title,
            String description,
            String location,
            Set<WeatherCondition> weatherConditionAvailability,
            Set<EventCategory> categories) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.weatherConditionAvailability = weatherConditionAvailability;
        this.categories = categories;
    }

    public static Result<Event> create(
            Long id,
            String title,
            String description,
            String location,
            String weatherConditionAvailability,
            Set<Result<EventCategory>> categoriesOrError) {
        if (categoriesOrError.stream().anyMatch(Result::isFailure)) {
            return Result.fromError(
                    categoriesOrError.stream().filter(Result::isFailure).findFirst().get());
        }

        Set<Result<WeatherCondition>> weatherConditionsOrError =
                Arrays.stream(weatherConditionAvailability.split(";"))
                        .map(WeatherCondition::create)
                        .collect(Collectors.toSet());

        if (weatherConditionsOrError.stream().anyMatch(Result::isFailure)) {
            return Result.fromError(
                    weatherConditionsOrError.stream().filter(Result::isFailure).findFirst().get());
        }

        Event event =
                new Event(
                        id,
                        title,
                        description,
                        location,
                        weatherConditionsOrError.stream()
                                .map(Result::getValue)
                                .collect(Collectors.toSet()),
                        categoriesOrError.stream()
                                .map(Result::getValue)
                                .collect(Collectors.toSet()));

        return Result.success(event);
    }

    public static Result<Event> create(
            String title,
            String description,
            String location,
            Set<WeatherCondition> weatherConditions,
            Set<EventCategory> categories) {

        if (Helpers.isNullOrEmpty(title) || title.length() > 255) {
            return Result.invalid(ResultErrorMessages.invalidEventTitle);
        }

        if (description.length() > 255){
          return Result.invalid(ResultErrorMessages.invalidEventDescription);
        }

        if (Helpers.isNullOrEmpty(location) || location.length() > 255) {
            return Result.invalid(ResultErrorMessages.invalidEventLocation);
        }

        Event event = new Event(0L, title, description, location, weatherConditions, categories);

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

    public Set<WeatherCondition> getWeatherConditionAvailability() {
        return weatherConditionAvailability;
    }

    public Set<EventCategory> getCategories() {
        return categories;
    }
}
