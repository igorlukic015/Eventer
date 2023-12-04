package com.eventer.admin.service;

import com.eventer.admin.common.dto.EventCategoryDTO;
import com.eventer.admin.model.EventCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface EventCategoryService {
    Optional<EventCategory> create(EventCategoryDTO createRequest);

    Page<EventCategory> getCategories(Pageable pageable);
}
