package com.eventer.admin.web.dto.event;

import java.util.Set;

public record CreateEventDTO(
        String title,
        String description,
        String location,
        String date,
        Set<String> weatherConditions,
        Set<Long> eventCategories,
        Set<String> savedImages) {}
