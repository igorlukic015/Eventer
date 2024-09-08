package com.eventer.admin.web.dto.event;

import com.eventer.admin.web.dto.eventcategory.EventCategoryDTO;

import java.time.Instant;
import java.util.Set;

public record EventDTO(
        Long id,
        String title,
        String description,
        String location,
        Instant date,
        Set<String> weatherConditions,
        Set<EventCategoryDTO> categories,
        Set<ImageDTO> images) {}
