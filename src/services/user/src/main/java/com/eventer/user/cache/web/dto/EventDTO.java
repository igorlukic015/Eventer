package com.eventer.user.cache.web.dto;

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
