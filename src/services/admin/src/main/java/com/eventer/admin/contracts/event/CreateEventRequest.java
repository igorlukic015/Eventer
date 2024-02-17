package com.eventer.admin.contracts.event;

import com.eventer.admin.service.domain.EventCategory;
import com.eventer.admin.service.domain.Image;
import com.eventer.admin.service.domain.WeatherCondition;
import com.eventer.admin.utils.Result;

import java.time.Instant;
import java.util.Set;

public record CreateEventRequest(
        String title,
        String description,
        String location,
        Result<Instant> dateOrError,
        Result<Set<WeatherCondition>> weatherConditionsOrError,
        Result<Set<EventCategory>> eventCategoriesOrError,
        Result<Set<Image>> imagesOrError) {}
