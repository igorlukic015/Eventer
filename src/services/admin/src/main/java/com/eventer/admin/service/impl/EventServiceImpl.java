package com.eventer.admin.service.impl;

import com.eventer.admin.contracts.ApplicationStatics;
import com.eventer.admin.contracts.event.CreateEventRequest;
import com.eventer.admin.contracts.message.Message;
import com.eventer.admin.contracts.message.MessageStatics;
import com.eventer.admin.data.repository.ImageRepository;
import com.eventer.admin.mapper.ImageMapper;
import com.eventer.admin.service.EventCategoryService;
import com.eventer.admin.service.MessageSenderService;
import com.eventer.admin.service.domain.Event;
import com.eventer.admin.mapper.EventMapper;
import com.eventer.admin.data.repository.EventRepository;
import com.eventer.admin.service.EventService;
import com.eventer.admin.service.domain.EventCategory;
import com.eventer.admin.service.domain.Image;
import com.eventer.admin.utils.Helpers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cigor99.resulter.Result;

import com.eventer.admin.utils.ResultErrorMessages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventCategoryService eventCategoryService;
    private final ImageRepository imageRepository;
    private final MessageSenderService messageSenderService;
    private final ObjectMapper objectMapper;

    public EventServiceImpl(
            EventRepository eventRepository,
            EventCategoryService eventCategoryService,
            ImageRepository imageRepository,
            MessageSenderService messageSenderService,
            ObjectMapper objectMapper) {
        this.eventRepository = eventRepository;
        this.eventCategoryService = eventCategoryService;
        this.imageRepository = imageRepository;
        this.messageSenderService = messageSenderService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Result<Event> create(CreateEventRequest createEventRequest) {
        if (createEventRequest.weatherConditionsOrError().isFailure()) {
            Helpers.deleteFilesFromPathSet(createEventRequest.savedImages());
            return Result.fromError(createEventRequest.weatherConditionsOrError());
        }

        if (createEventRequest.eventCategoriesOrError().isFailure()) {
            Helpers.deleteFilesFromPathSet(createEventRequest.savedImages());
            return Result.fromError(createEventRequest.eventCategoriesOrError());
        }

        if (createEventRequest.dateOrError().isFailure()) {
            Helpers.deleteFilesFromPathSet(createEventRequest.savedImages());
            return Result.fromError(createEventRequest.dateOrError());
        }

        Result<Set<EventCategory>> categoriesOrError =
                this.eventCategoryService.getCategoriesByIds(
                        createEventRequest.eventCategoriesOrError().getValue().stream()
                                .map(EventCategory::getId)
                                .collect(Collectors.toSet()));

        if (categoriesOrError.isFailure()) {
            Helpers.deleteFilesFromPathSet(createEventRequest.savedImages());
            return Result.fromError(categoriesOrError);
        }

        Set<Image> savedImages = new HashSet<>();

        for (Path savedImagePath : createEventRequest.savedImages()) {
            String imageName = savedImagePath.toString().replace("\\", "/");

            Result<Image> imageOrError = Image.create(imageName);

            if (imageOrError.isFailure()) {
                Helpers.deleteFilesFromPathSet(createEventRequest.savedImages());
                return Result.invalid(ResultErrorMessages.invalidImage);
            }

            com.eventer.admin.data.model.Image savedimage;
            try {
                savedimage =
                        this.imageRepository.save(
                                ImageMapper.toModel(
                                        imageOrError.getValue(), Event.class.getSimpleName()));
                imageOrError.getValue().setId(savedimage.getId());
            } catch (Exception e) {
                Helpers.deleteFilesFromPathSet(createEventRequest.savedImages());
                throw e;
            }

            savedImages.add(imageOrError.getValue());
        }

        Result<Event> eventOrError =
                Event.create(
                        createEventRequest.title(),
                        createEventRequest.description(),
                        createEventRequest.location(),
                        createEventRequest.dateOrError().getValue(),
                        createEventRequest.weatherConditionsOrError().getValue(),
                        categoriesOrError.getValue(),
                        savedImages);

        if (eventOrError.isFailure()) {
            Helpers.deleteFilesFromPathSet(createEventRequest.savedImages());
            return Result.fromError(eventOrError);
        }

        com.eventer.admin.data.model.Event event =
                EventMapper.toModel(eventOrError.getValue(), savedImages);

        try {
            event = this.eventRepository.save(event);
        } catch (Exception e) {
            Helpers.deleteFilesFromPathSet(createEventRequest.savedImages());
            throw e;
        }

        Result<Event> createdEventOrError = EventMapper.toDomain(event);

        if (createdEventOrError.isFailure()) {
            return Result.internalError(ResultErrorMessages.failedToSendMessage);
        }

        Result messageSentOrError =
                this.sendMessage(MessageStatics.ACTION_CREATED, createdEventOrError.getValue());

        if (messageSentOrError.isFailure()) {
            return Result.internalError(ResultErrorMessages.failedToSendMessage);
        }

        return createdEventOrError;
    }

    @Override
    public Result<Page<Event>> getEvents(Pageable pageable) {
        Page<com.eventer.admin.data.model.Event> foundEvents =
                this.eventRepository.findAll(pageable);

        Result<Page<Event>> eventsOrError = EventMapper.toDomainPage(foundEvents);

        if (eventsOrError.isFailure()) {
            return Result.fromError(eventsOrError);
        }

        return eventsOrError;
    }

    private Result sendMessage(String action, Event event) {
        Message eventMessage =
                new Message(
                        MessageStatics.NAME_ENTITY_UPDATED,
                        Instant.now(),
                        Event.class.getSimpleName(),
                        action,
                        event);

        String messagePayload;
        try {
            messagePayload = this.objectMapper.writeValueAsString(eventMessage);
        } catch (JsonProcessingException e) {
            return Result.internalError(ResultErrorMessages.failedToSendMessage);
        }

        this.messageSenderService.sendMessage(
                ApplicationStatics.EVENTER_DATA_MESSAGE_QUEUE, messagePayload);

        return Result.success();
    }
}
