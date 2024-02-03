package com.eventer.admin.domain;

import java.time.Instant;
import java.util.Set;

public class Event {
    private Long id;

    private String name;

    private String description;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Set<EventCategory> categories;

    public Event(Long id,
                 String name,
                 String description,
                 String createdBy,
                 Instant createdDate,
                 String lastModifiedBy,
                 Instant lastModifiedDate,
                 Set<EventCategory> categories) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
        this.categories = categories;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Set<EventCategory> getCategories() {
        return categories;
    }
}
