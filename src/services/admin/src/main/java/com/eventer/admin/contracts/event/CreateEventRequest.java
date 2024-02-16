package com.eventer.admin.contracts.event;

import com.eventer.admin.service.domain.EventCategory;
import com.eventer.admin.service.domain.WeatherCondition;
import com.eventer.admin.utils.Result;

import java.util.Set;

public record CreateEventRequest(
        String title,
        String description,
        String location,
        Set<Result<WeatherCondition>> weatherConditions,
        Set<Result<EventCategory>> eventCategories) {
    public CreateEventRequest(
            String title,
            String description,
            String location,
            Set<Result<WeatherCondition>> weatherConditions,
            Set<Result<EventCategory>> eventCategories) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.weatherConditions = weatherConditions;
        this.eventCategories = eventCategories;
    }
}
