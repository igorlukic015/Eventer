package com.eventer.admin.service;

import com.eventer.admin.contracts.eventcategory.CreateEventCategoryRequest;
import com.github.cigor99.resulter.Result;
import com.eventer.admin.service.domain.EventCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface EventCategoryService {
    Result<EventCategory> create(CreateEventCategoryRequest request);

    Result<Page<EventCategory>> getCategories(Pageable pageable);

    Result<Set<EventCategory>> getCategoriesByIds(Set<Long> ids);
}
