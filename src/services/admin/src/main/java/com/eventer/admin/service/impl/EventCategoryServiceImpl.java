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
import com.github.igorlukic015.resulter.Result;
import com.eventer.admin.utils.ResultErrorMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
public class EventCategoryServiceImpl implements EventCategoryService {

    private final EventCategoryRepository eventCategoryRepository;
    private final MessageSenderService messageSenderService;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(EventCategoryServiceImpl.class);

    public EventCategoryServiceImpl(
            EventCategoryRepository eventCategoryRepository,
            MessageSenderService messageSenderService,
            ObjectMapper objectMapper) {
        this.eventCategoryRepository = eventCategoryRepository;
        this.messageSenderService = messageSenderService;
        this.objectMapper = objectMapper;
    }

    @Transactional
    @Override
    public Result<EventCategory> create(CreateEventCategoryRequest request) {
        logger.info(
                "Attempting to create a new {} with name: {}",
                EventCategory.class.getSimpleName(),
                request.name());

        boolean isDuplicate = this.eventCategoryRepository.existsByNameIgnoreCase(request.name());

        if (isDuplicate) {
            logger.error(ResultErrorMessages.categoryAlreadyExists);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.conflict(ResultErrorMessages.categoryAlreadyExists);
        }

        Result<EventCategory> newCategoryOrError =
                EventCategory.create(request.name(), request.description());

        if (newCategoryOrError.isFailure()) {
            logger.error(newCategoryOrError.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fromError(newCategoryOrError);
        }

        com.eventer.admin.data.model.EventCategory eventCategory =
                EventCategoryMapper.toModel(newCategoryOrError.getValue());

        com.eventer.admin.data.model.EventCategory result =
                this.eventCategoryRepository.save(eventCategory);

        logger.info(
                "New {} saved with ID: {}", EventCategory.class.getSimpleName(), result.getId());

        Result<EventCategory> createdCategoryOrError = EventCategoryMapper.toDomain(result);

        if (createdCategoryOrError.isFailure()) {
            logger.error(ResultErrorMessages.failedToSendMessage);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.internalError(ResultErrorMessages.failedToSendMessage);
        }

        Result messageSentOrError =
                this.sendMessage(MessageStatics.ACTION_CREATED, createdCategoryOrError.getValue());

        if (messageSentOrError.isFailure()) {
            logger.error(messageSentOrError.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fromError(messageSentOrError);
        }

        return createdCategoryOrError;
    }

    @Transactional(readOnly = true)
    @Override
    public Result<Page<EventCategory>> getCategories(Pageable pageable, String searchTerm) {
        logger.info("Attempting to get categories");

        Page<com.eventer.admin.data.model.EventCategory> foundCategories =
                this.eventCategoryRepository.findByNameContaining(searchTerm, pageable);

        Result<Page<EventCategory>> categoriesOrError =
                EventCategoryMapper.toDomainPage(foundCategories);

        if (categoriesOrError.isFailure()) {
            logger.error(categoriesOrError.getMessage());
            return Result.fromError(categoriesOrError);
        }

        return categoriesOrError;
    }

    @Transactional(readOnly = true)
    @Override
    public Result<Set<EventCategory>> getCategoriesByIds(Set<Long> ids) {
        logger.info(
                "Attempting to get categories by ids {}",
                String.join(",", ids.stream().map(Object::toString).toList()));

        List<com.eventer.admin.data.model.EventCategory> foundCategories =
                this.eventCategoryRepository.findAllById(ids);

        if (foundCategories.isEmpty()) {
            logger.error(ResultErrorMessages.categoriesNotFound);
            return Result.notFound(ResultErrorMessages.categoriesNotFound);
        }

        return EventCategoryMapper.toDomainSet(foundCategories);
    }

    @Override
    public Result<Set<EventCategory>> getAllCategories() {
        logger.info("Attempting to get all categories");

        List<com.eventer.admin.data.model.EventCategory> foundCategories = this.eventCategoryRepository.findAll();

        if (foundCategories.isEmpty()) {
            logger.error(ResultErrorMessages.categoriesNotFound);
            return Result.notFound(ResultErrorMessages.categoriesNotFound);
        }

        return EventCategoryMapper.toDomainSet(foundCategories);
    }

    private Result sendMessage(String action, EventCategory category) {
        logger.info(
                "Attempting to send message for action {} and {} id {}",
                action,
                EventCategory.class.getSimpleName(),
                category.getId());

        Message categoryMessage =
                new Message(
                        MessageStatics.NAME_ENTITY_UPDATED,
                        Instant.now(),
                        EventCategory.class.getSimpleName(),
                        action,
                        category);

        String messagePayload;
        try {
            messagePayload = this.objectMapper.writeValueAsString(categoryMessage);
        } catch (JsonProcessingException e) {
            logger.error("{} with exception {}", ResultErrorMessages.failedToSendMessage, e.getMessage());
            return Result.internalError(ResultErrorMessages.failedToSendMessage);
        }

        this.messageSenderService.sendMessage(
                ApplicationStatics.EVENTER_DATA_MESSAGE_QUEUE, messagePayload);

        logger.info("Message sent successfully");

        return Result.success();
    }
}
