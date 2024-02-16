package com.eventer.admin.service;

import com.eventer.admin.contracts.eventcategory.CreateEventCategoryRequest;
import com.eventer.admin.utils.Result;
import com.eventer.admin.service.domain.EventCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventCategoryService {
    Result<EventCategory> create(CreateEventCategoryRequest request);

    Result<Page<EventCategory>> getCategories(Pageable pageable);
}
