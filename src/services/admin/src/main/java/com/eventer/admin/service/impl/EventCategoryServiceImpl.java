package com.eventer.admin.service.impl;

import com.eventer.admin.mapper.EventCategoryMapper;
import com.eventer.admin.utils.Result;
import com.eventer.admin.domain.EventCategory;
import com.eventer.admin.repository.EventCategoryRepository;
import com.eventer.admin.service.EventCategoryService;
import com.eventer.admin.utils.ResultErrorMessages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EventCategoryServiceImpl implements EventCategoryService {

    private final EventCategoryRepository eventCategoryRepository;

    public EventCategoryServiceImpl(EventCategoryRepository eventCategoryRepository) {
        this.eventCategoryRepository = eventCategoryRepository;
    }

    @Override
    public Result<EventCategory> create(String name, String description) {
        boolean isDuplicate = this.eventCategoryRepository.existsByNameIgnoreCase(name);

        if (isDuplicate) {
            return Result.conflict(ResultErrorMessages.categoryAlreadyExists);
        }

        Result<EventCategory> newCategoryOrError = EventCategory.create(name, description);

        if (newCategoryOrError.isFailure()) {
            return Result.fromError(newCategoryOrError);
        }

        com.eventer.admin.model.EventCategory eventCategory =
                EventCategoryMapper.toModel(newCategoryOrError.getValue(), "SYSTEM");

        com.eventer.admin.model.EventCategory result = this.eventCategoryRepository.save(eventCategory);

        return EventCategoryMapper.toDomain(result);
    }

    @Override
    public Result<Page<EventCategory>> getCategories(Pageable pageable) {
        Page<com.eventer.admin.model.EventCategory> foundCategories = this.eventCategoryRepository.findAll(pageable);

        Result<Page<EventCategory>> categoriesOrError = EventCategoryMapper.toDomainPage(foundCategories);

        if (categoriesOrError.isFailure()) {
            return Result.fromError(categoriesOrError);
        }

        return categoriesOrError;
    }
}
