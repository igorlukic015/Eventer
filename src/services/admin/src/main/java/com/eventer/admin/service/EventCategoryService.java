package com.eventer.admin.service;

import com.eventer.admin.contracts.eventcategory.CreateEventCategoryRequest;
import com.eventer.admin.contracts.eventcategory.UpdateEventCategoryRequest;
import com.github.igorlukic015.resulter.Result;
import com.eventer.admin.service.domain.EventCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface EventCategoryService {
    Result<EventCategory> create(CreateEventCategoryRequest request);

    Result<Page<EventCategory>> getCategories(Pageable pageable, String searchTerm);

    Result<Set<EventCategory>> getCategoriesByIds(Set<Long> ids);

    Result<Set<EventCategory>> getAllCategories();

    Result delete(Long id);

    Result<EventCategory> update(UpdateEventCategoryRequest request);
}
