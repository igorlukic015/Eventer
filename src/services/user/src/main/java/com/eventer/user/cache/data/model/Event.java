package com.eventer.user.cache.data.model;

import com.eventer.user.cache.web.dto.EventCategoryDTO;
import com.eventer.user.cache.web.dto.ImageDTO;
import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;
import com.redis.om.spring.annotations.Searchable;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

@Document
public class Event {
    @Id private String id;

    @Indexed private Long eventId;

    @Searchable private String title;

    @Searchable private String description;

    private String location;

    private Instant date;

    @Searchable private Set<String> weatherConditions;

    @Searchable private Set<EventCategoryDTO> categories;

    private Set<ImageDTO> images;

    public Event() {}

    public Event(
            Long eventId,
            String title,
            String description,
            String location,
            Set<String> weatherConditions,
            Set<EventCategoryDTO> categories,
            Set<ImageDTO> images) {
        this.id = null;
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.weatherConditions = weatherConditions;
        this.categories = categories;
        this.images = images;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) || Objects.equals(eventId, event.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
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

    public Set<EventCategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(Set<EventCategoryDTO> categories) {
        this.categories = categories;
    }

    public Set<ImageDTO> getImages() {
        return images;
    }

    public void setImages(Set<ImageDTO> images) {
        this.images = images;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }
}
