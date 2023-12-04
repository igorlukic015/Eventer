package com.eventer.admin.domain;

import java.time.Instant;
import java.util.Optional;

public class EventCategory {

    private Long id;

    private String name;

    private String description;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private EventCategory(Long id,
                          String name,
                          String description,
                          String createdBy,
                          Instant createdDate,
                          String lastModifiedBy,
                          Instant lastModifiedDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
    }

    public Optional<EventCategory> create(Long id,
                                          String name,
                                          String description,
                                          String createdBy,
                                          Instant createdDate) {
        EventCategory eventCategory =
                new EventCategory(
                        id,
                        name,
                        description,
                        "system",
                        Instant.now(),
                        "system",
                        Instant.now());

        return Optional.of(eventCategory);
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
}
