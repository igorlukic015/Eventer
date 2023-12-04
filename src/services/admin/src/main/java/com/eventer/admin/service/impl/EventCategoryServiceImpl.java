package com.eventer.admin.service.impl;

import com.eventer.admin.common.dto.EventCategoryDTO;
import com.eventer.admin.model.EventCategory;
import com.eventer.admin.repository.EventCategoryRepository;
import com.eventer.admin.service.EventCategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;


@Service
public class EventCategoryServiceImpl implements EventCategoryService {

    private final EventCategoryRepository eventCategoryRepository;

    public EventCategoryServiceImpl(EventCategoryRepository eventCategoryRepository) {
        this.eventCategoryRepository = eventCategoryRepository;
    }

    @Override
    public Optional<EventCategory> create(EventCategoryDTO createRequest) {
        EventCategory newCategory = new EventCategory();

        newCategory.setName(createRequest.name());
        newCategory.setDescription(createRequest.description());
        newCategory.setCreatedBy("system");
        newCategory.setCreatedDate(Instant.now());
        newCategory.setLastModifiedBy("system");
        newCategory.setLastModifiedDate(Instant.now());

        var savedCategory = this.eventCategoryRepository.save(newCategory);

        return Optional.of(savedCategory);
    }

    @Override
    public Page<EventCategory> getCategories(Pageable pageable) {
        return null;
    }
}
