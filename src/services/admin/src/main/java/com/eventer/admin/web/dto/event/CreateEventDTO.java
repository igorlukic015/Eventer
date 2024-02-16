package com.eventer.admin.web.dto.event;

import com.eventer.admin.web.dto.eventcategory.EventCategoryDTO;

import java.util.Set;

public record CreateEventDTO(
        String title,
        String description,
        String location,
        Set<String> weatherConditions,
        Set<EventCategoryDTO> eventCategories) {

    public CreateEventDTO(
            String title,
            String description,
            String location,
            Set<String> weatherConditions,
            Set<EventCategoryDTO> eventCategories) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.weatherConditions = weatherConditions;
        this.eventCategories = eventCategories;
    }
}
