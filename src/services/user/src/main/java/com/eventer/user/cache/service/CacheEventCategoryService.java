package com.eventer.user.cache.service;

import com.eventer.user.cache.data.model.EventCategory;

import java.util.Set;

public interface CacheEventCategoryService {
    void reloadCategories();

    Set<EventCategory> getAll();

    void add(EventCategory newCategory);

    void update(EventCategory updatedCategory);

    void remove(Long deletedId);
}
