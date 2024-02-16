package com.eventer.admin.web.dto.event;

import com.eventer.admin.web.dto.eventcategory.EventCategoryDTO;

import java.util.Set;

public record EventDTO(
        Long id,
        String title,
        String description,
        String location,
        Set<String> weatherConditions,
        Set<EventCategoryDTO> categories) {

    public EventDTO(
            Long id,
            String title,
            String description,
            String location,
            Set<String> weatherConditions,
            Set<EventCategoryDTO> categories) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.weatherConditions = weatherConditions;
        this.categories = categories;
    }
}
