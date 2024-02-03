package com.eventer.admin.service;

import com.eventer.admin.utils.Result;
import com.eventer.admin.domain.EventCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventCategoryService {
    Result<EventCategory> create(String name, String description);

    Result<Page<EventCategory>> getCategories(Pageable pageable);
}
