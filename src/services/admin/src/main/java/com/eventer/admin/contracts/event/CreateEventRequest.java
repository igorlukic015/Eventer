package com.eventer.admin.contracts.event;

import com.eventer.admin.service.domain.WeatherCondition;
import com.github.igorlukic015.resulter.Result;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Set;

public record CreateEventRequest(
        String title,
        String description,
        String location,
        Result<Instant> dateOrError,
        Result<Set<WeatherCondition>> weatherConditionsOrError,
        Set<Long> eventCategoryIds,
        Set<Path> savedImages) {}
