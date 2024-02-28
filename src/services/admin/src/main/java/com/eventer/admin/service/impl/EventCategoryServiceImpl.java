package com.eventer.admin.service.impl;

import com.eventer.admin.contracts.ApplicationStatics;
import com.eventer.admin.contracts.message.Message;
import com.eventer.admin.contracts.eventcategory.CreateEventCategoryRequest;
import com.eventer.admin.contracts.message.MessageStatics;
import com.eventer.admin.service.MessageSenderService;
import com.eventer.admin.service.domain.EventCategory;
import com.eventer.admin.mapper.EventCategoryMapper;
import com.eventer.admin.data.repository.EventCategoryRepository;
import com.eventer.admin.service.EventCategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cigor99.resulter.Result;
import com.eventer.admin.utils.ResultErrorMessages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
public class EventCategoryServiceImpl implements EventCategoryService {

    private final EventCategoryRepository eventCategoryRepository;
    private final MessageSenderService messageSenderService;
    private final ObjectMapper objectMapper;

    public EventCategoryServiceImpl(
            EventCategoryRepository eventCategoryRepository,
            MessageSenderService messageSenderService,
            ObjectMapper objectMapper) {
        this.eventCategoryRepository = eventCategoryRepository;
        this.messageSenderService = messageSenderService;
        this.objectMapper = objectMapper;
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

        Result<EventCategory> createdCategoryOrError = EventCategoryMapper.toDomain(result);

        if (createdCategoryOrError.isFailure()) {
            return Result.internalError(ResultErrorMessages.failedToSendMessage);
        }

        Result messageSentOrError =
                this.sendMessage(MessageStatics.ACTION_CREATED, createdCategoryOrError.getValue());

        if (messageSentOrError.isFailure()) {
            return Result.fromError(messageSentOrError);
        }

        return createdCategoryOrError;
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

    public Result sendMessage(String action, EventCategory category) {
        Message categoryCreatedMessage =
                new Message(
                        MessageStatics.NAME_ENTITY_UPDATED,
                        Instant.now(),
                        EventCategory.class.getSimpleName(),
                        action,
                        category);

        String messagePayload;
        try {
            messagePayload = this.objectMapper.writeValueAsString(categoryCreatedMessage);
        } catch (JsonProcessingException e) {
            return Result.internalError(ResultErrorMessages.failedToSendMessage);
        }

        this.messageSenderService.sendMessage(
                ApplicationStatics.EVENTER_DATA_MESSAGE_QUEUE, messagePayload);

        return Result.success();
    }
}
