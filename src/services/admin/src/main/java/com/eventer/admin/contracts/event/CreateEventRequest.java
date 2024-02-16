package com.eventer.admin.contracts.event;

import com.eventer.admin.service.domain.EventCategory;
import com.eventer.admin.service.domain.WeatherCondition;
import com.eventer.admin.utils.Result;

import java.util.Set;

public record CreateEventRequest(
        String title,
        String description,
        String location,
        Result<Set<WeatherCondition>> weatherConditionsOrError,
        Result<Set<EventCategory>> eventCategoriesOrError) {
    public CreateEventRequest(
            String title,
            String description,
            String location,
            Result<Set<WeatherCondition>> weatherConditionsOrError,
            Result<Set<EventCategory>> eventCategoriesOrError) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.weatherConditionsOrError = weatherConditionsOrError;
        this.eventCategoriesOrError = eventCategoriesOrError;
    }
}
