package com.eventer.user.cache.service;

import com.eventer.user.cache.data.model.EventCategory;
import com.eventer.user.cache.web.dto.EventCategoryDTO;

import java.util.Set;

public interface EventCategoryService {
    void reloadCategories();

    Set<EventCategory> getAll();
}
