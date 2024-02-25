package com.eventer.admin.service.impl;

import com.eventer.admin.contracts.ApplicationStatics;
import com.eventer.admin.contracts.eventcategory.CreateEventCategoryRequest;
import com.eventer.admin.service.MessageSenderService;
import com.eventer.admin.service.domain.EventCategory;
import com.eventer.admin.mapper.EventCategoryMapper;
import com.eventer.admin.data.repository.EventCategoryRepository;
import com.eventer.admin.service.EventCategoryService;
import com.github.cigor99.resulter.Result;
import com.eventer.admin.utils.ResultErrorMessages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class EventCategoryServiceImpl implements EventCategoryService {

    private final EventCategoryRepository eventCategoryRepository;
    private final MessageSenderService messageSenderService;

    public EventCategoryServiceImpl(EventCategoryRepository eventCategoryRepository, MessageSenderService messageSenderService) {
        this.eventCategoryRepository = eventCategoryRepository;
        this.messageSenderService = messageSenderService;
    }

    @Override
    public Result<EventCategory> create(CreateEventCategoryRequest request) {
        boolean isDuplicate = this.eventCategoryRepository.existsByNameIgnoreCase(request.name());

        if (isDuplicate) {
            return Result.conflict(ResultErrorMessages.categoryAlreadyExists);
        }

        Result<EventCategory> newCategoryOrError =
                EventCategory.create(request.name(), request.description());

        if (newCategoryOrError.isFailure()) {
            return Result.fromError(newCategoryOrError);
        }

        com.eventer.admin.data.model.EventCategory eventCategory =
                EventCategoryMapper.toModel(newCategoryOrError.getValue());

        com.eventer.admin.data.model.EventCategory result =
                this.eventCategoryRepository.save(eventCategory);

        return EventCategoryMapper.toDomain(result);
    }

    @Override
    public Result<Page<EventCategory>> getCategories(Pageable pageable) {
        Page<com.eventer.admin.data.model.EventCategory> foundCategories =
                this.eventCategoryRepository.findAll(pageable);

        Result<Page<EventCategory>> categoriesOrError =
                EventCategoryMapper.toDomainPage(foundCategories);

        if (categoriesOrError.isFailure()) {
            return Result.fromError(categoriesOrError);
        }

        return categoriesOrError;
    }

    @Override
    public Result<Set<EventCategory>> getCategoriesByIds(Set<Long> ids) {
        List<com.eventer.admin.data.model.EventCategory> foundCategories =
                this.eventCategoryRepository.findAllById(ids);

        if (foundCategories.size() == 0) {
            return Result.notFound(ResultErrorMessages.categoriesNotFound);
        }

        return EventCategoryMapper.toDomainSet(foundCategories);
    }

    @Override
    public void testMessages() {
        List<com.eventer.admin.data.model.EventCategory> foundCategories =
                this.eventCategoryRepository.findAll();

        String msg = foundCategories.stream().findFirst().orElseThrow().getName();

        this.messageSenderService.sendMessage(ApplicationStatics.EVENTER_DATA_MESSAGE_QUEUE, msg);
    }
}
