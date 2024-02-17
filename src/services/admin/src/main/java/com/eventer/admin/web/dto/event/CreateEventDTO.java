package com.eventer.admin.web.dto.event;

import com.eventer.admin.web.dto.eventcategory.EventCategoryDTO;

import java.util.Set;

public class CreateEventDTO {
    private String title;
    private String description;
    private String location;
    private String date;
    private Set<String> weatherConditions;
    private Set<EventCategoryDTO> eventCategories;

    public CreateEventDTO() {}

    public CreateEventDTO(
            String title,
            String description,
            String location,
            String date,
            Set<String> weatherConditions,
            Set<EventCategoryDTO> eventCategories) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.weatherConditions = weatherConditions;
        this.eventCategories = eventCategories;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Set<String> getWeatherConditions() {
        return weatherConditions;
    }

    public void setWeatherConditions(Set<String> weatherConditions) {
        this.weatherConditions = weatherConditions;
    }

    public Set<EventCategoryDTO> getEventCategories() {
        return eventCategories;
    }

    public void setEventCategories(Set<EventCategoryDTO> eventCategories) {
        this.eventCategories = eventCategories;
    }
}
