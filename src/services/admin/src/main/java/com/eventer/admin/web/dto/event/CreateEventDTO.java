package com.eventer.admin.web.dto.event;

import com.eventer.admin.web.dto.eventcategory.EventCategoryDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public class CreateEventDTO {

    private String title;
    private String description;
    private String location;
    private Set<String> weatherConditions;
    private Set<EventCategoryDTO> eventCategories;
    private Set<MultipartFile> images;

    public CreateEventDTO() {}

    public CreateEventDTO(
            String title,
            String description,
            String location,
            Set<String> weatherConditions,
            Set<EventCategoryDTO> eventCategories,
            Set<MultipartFile> images) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.weatherConditions = weatherConditions;
        this.eventCategories = eventCategories;
        this.images = images;
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

    public Set<MultipartFile> getImages() {
        return images;
    }

    public void setImages(Set<MultipartFile> images) {
        this.images = images;
    }
}
