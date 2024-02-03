package com.eventer.admin.domain;

import com.eventer.admin.utils.Result;

import java.time.Instant;

public class EventCategory {

    private Long id;

    private String name;

    private String description;

    private EventCategory(Long id,
                          String name,
                          String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static Result<EventCategory> create(Long id,
                                               String name,
                                               String description) {
        EventCategory eventCategory =
                new EventCategory(
                        id,
                        name,
                        description);

        return Result.success(eventCategory);
    }

    public static Result<EventCategory> create(String name, String description) {
        EventCategory eventCategory =
                new EventCategory(
                        0L,
                        name,
                        description);

        return Result.success(eventCategory);
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
}
