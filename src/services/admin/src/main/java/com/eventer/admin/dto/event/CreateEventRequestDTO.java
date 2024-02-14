package com.eventer.admin.dto.event;

import com.eventer.admin.domain.EventCategory;

import java.util.Set;

public record CreateEventRequestDTO(String title, String description, String location,
                                    Set<String> weatherConditions, Set<EventCategory> eventCategories) {
    public CreateEventRequestDTO(String title, String description, String location,
                                 Set<String> weatherConditions, Set<EventCategory> eventCategories) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.weatherConditions = weatherConditions;
        this.eventCategories = eventCategories;
    }
}
