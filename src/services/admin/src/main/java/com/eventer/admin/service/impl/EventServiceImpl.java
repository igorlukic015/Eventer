package com.eventer.admin.service.impl;

import com.eventer.admin.contracts.ApplicationStatics;
import com.eventer.admin.contracts.event.CreateEventRequest;
import com.eventer.admin.contracts.event.UpdateEventRequest;
import com.eventer.admin.contracts.message.Message;
import com.eventer.admin.contracts.message.MessageStatics;
import com.eventer.admin.data.repository.ImageRepository;
import com.eventer.admin.mapper.EventCategoryMapper;
import com.eventer.admin.mapper.ImageMapper;
import com.eventer.admin.service.EventCategoryService;
import com.eventer.admin.service.ImageHostService;
import com.eventer.admin.service.MessageSenderService;
import com.eventer.admin.service.domain.Event;
import com.eventer.admin.mapper.EventMapper;
import com.eventer.admin.data.repository.EventRepository;
import com.eventer.admin.service.EventService;
import com.eventer.admin.service.domain.EventCategory;
import com.eventer.admin.service.domain.Image;
import com.eventer.admin.service.domain.WeatherCondition;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventCategoryService eventCategoryService;
    private final ImageRepository imageRepository;
    private final ImageHostService imageHostService;
    private final MessageSenderService messageSenderService;
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    public EventServiceImpl(
            EventRepository eventRepository,
            EventCategoryService eventCategoryService,
            ImageRepository imageRepository,
            ImageHostService imageHostService,
            MessageSenderService messageSenderService,
            ObjectMapper objectMapper) {
        this.eventRepository = eventRepository;
        this.eventCategoryService = eventCategoryService;
        this.imageRepository = imageRepository;
        this.imageHostService = imageHostService;
        this.messageSenderService = messageSenderService;
        this.objectMapper = objectMapper;
    }

    @Transactional
    @Override
    public Result<Event> create(CreateEventRequest createEventRequest) {
        logger.info("Attempting to create event {}", createEventRequest.title());

        if (createEventRequest.weatherConditionsOrError().isFailure()) {
            logger.error(createEventRequest.weatherConditionsOrError().getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            this.imageHostService.deleteAll(createEventRequest.savedImages());
            return Result.fromError(createEventRequest.weatherConditionsOrError());
        }

        if (createEventRequest.dateOrError().isFailure()) {
            logger.error(createEventRequest.dateOrError().getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            this.imageHostService.deleteAll(createEventRequest.savedImages());
            return Result.fromError(createEventRequest.dateOrError());
        }

        Result<Set<EventCategory>> categoriesOrError =
                this.eventCategoryService.getCategoriesByIds(createEventRequest.eventCategoryIds());

        if (categoriesOrError.isFailure()) {
            logger.error(categoriesOrError.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            this.imageHostService.deleteAll(createEventRequest.savedImages());
            return Result.fromError(categoriesOrError);
        }

        Set<Image> savedImages = new HashSet<>();

        for (String imageName : createEventRequest.savedImages()) {
            Result<Image> imageOrError = Image.create(imageName);

            if (imageOrError.isFailure()) {
                logger.error(ResultErrorMessages.invalidImage);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                this.imageHostService.deleteAll(createEventRequest.savedImages());
                return Result.invalid(ResultErrorMessages.invalidImage);
            }

            com.eventer.admin.data.model.Image savedimage;
            try {
                savedimage =
                        this.imageRepository.save(ImageMapper.toModel(imageOrError.getValue()));
                imageOrError.getValue().setId(savedimage.getId());
            } catch (Exception e) {
                logger.error(e.getMessage());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                this.imageHostService.deleteAll(createEventRequest.savedImages());
                return Result.invalid(e.getMessage());
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
            logger.error(eventOrError.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            this.imageHostService.deleteAll(createEventRequest.savedImages());
            return Result.fromError(eventOrError);
        }

        com.eventer.admin.data.model.Event event =
                EventMapper.toModel(eventOrError.getValue(), savedImages);

        try {
            event = this.eventRepository.save(event);
        } catch (Exception e) {
            logger.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            this.imageHostService.deleteAll(createEventRequest.savedImages());
            return Result.invalid(e.getMessage());
        }

        Result<Event> createdEventOrError = EventMapper.toDomain(event);

        if (createdEventOrError.isFailure()) {
            logger.error(ResultErrorMessages.failedToSendMessage);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.internalError(ResultErrorMessages.failedToSendMessage);
        }

        Result messageSentOrError =
                this.sendMessage(
                        MessageStatics.ACTION_CREATED,
                        createdEventOrError.getValue().getId(),
                        createdEventOrError.getValue());

        if (messageSentOrError.isFailure()) {
            logger.error(ResultErrorMessages.failedToSendMessage);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.internalError(ResultErrorMessages.failedToSendMessage);
        }

        logger.info("{} created successfully", Event.class.getSimpleName());

        return createdEventOrError;
    }

    @Transactional
    @Override
    public Result<Event> update(UpdateEventRequest request) {
        logger.info("Attempting to update event {}", request.title());

        Optional<com.eventer.admin.data.model.Event> foundEvent =
                this.eventRepository.findById(request.id());

        if (foundEvent.isEmpty()) {
            logger.error(ResultErrorMessages.eventNotFound);
            return Result.notFound(ResultErrorMessages.eventNotFound);
        }

        Set<String> newSavedImages =
                request.savedImages().stream()
                        .filter(
                                savedImage ->
                                        !foundEvent.get().getImages().stream()
                                                .map(com.eventer.admin.data.model.Image::getName)
                                                .toList()
                                                .contains(savedImage))
                        .collect(Collectors.toSet());

        if (request.weatherConditionsOrError().isFailure()) {
            logger.error(request.weatherConditionsOrError().getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            this.imageHostService.deleteAll(newSavedImages);
            return Result.fromError(request.weatherConditionsOrError());
        }

        if (request.dateOrError().isFailure()) {
            logger.error(request.dateOrError().getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            this.imageHostService.deleteAll(newSavedImages);
            return Result.fromError(request.dateOrError());
        }

        Result<Set<EventCategory>> categoriesOrError =
                this.eventCategoryService.getCategoriesByIds(request.eventCategoryIds());

        if (categoriesOrError.isFailure()) {
            logger.error(categoriesOrError.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            this.imageHostService.deleteAll(newSavedImages);
            return Result.fromError(categoriesOrError);
        }

        Set<com.eventer.admin.data.model.Image> savedImageEntities = new HashSet<>();
        Set<com.eventer.admin.data.model.Image> oldImages = foundEvent.get().getImages();

        for (String imageName : request.savedImages()) {
            Optional<com.eventer.admin.data.model.Image> foundImage =
                    this.imageRepository.findByName(imageName);

            if (foundImage.isPresent()) {
                savedImageEntities.add(foundImage.get());
                continue;
            }

            Result<Image> imageOrError = Image.create(imageName);

            if (imageOrError.isFailure()) {
                logger.error(ResultErrorMessages.invalidImage);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                this.imageHostService.deleteAll(newSavedImages);
                return Result.invalid(ResultErrorMessages.invalidImage);
            }

            com.eventer.admin.data.model.Image savedimage;
            try {
                savedimage =
                        this.imageRepository.save(ImageMapper.toModel(imageOrError.getValue()));
            } catch (Exception e) {
                logger.error(e.getMessage());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                this.imageHostService.deleteAll(newSavedImages);
                return Result.invalid(e.getMessage());
            }

            savedImageEntities.add(savedimage);
        }

        foundEvent.get().setTitle(request.title());
        foundEvent.get().setDescription(request.description());
        foundEvent.get().setLocation(request.location());
        foundEvent.get().setDate(request.dateOrError().getValue());

        foundEvent
                .get()
                .setWeatherConditionAvailability(
                        String.join(
                                WeatherCondition.serializationSeparator,
                                request.weatherConditionsOrError().getValue().stream()
                                        .map(WeatherCondition::getName)
                                        .toList()));

        foundEvent
                .get()
                .setCategories(
                        categoriesOrError.getValue().stream()
                                .map(EventCategoryMapper::toModel)
                                .collect(Collectors.toSet()));

        foundEvent.get().setImages(savedImageEntities);

        com.eventer.admin.data.model.Event event;

        try {
            event = this.eventRepository.save(foundEvent.get());
        } catch (Exception e) {
            logger.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            this.imageHostService.deleteAll(newSavedImages);
            return Result.invalid(e.getMessage());
        }

        Result<Event> updatedEventOrError = EventMapper.toDomain(event);

        if (updatedEventOrError.isFailure()) {
            logger.error(ResultErrorMessages.failedToSendMessage);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.internalError(ResultErrorMessages.failedToSendMessage);
        }

        Result messageSentOrError =
                this.sendMessage(
                        MessageStatics.ACTION_UPDATED,
                        updatedEventOrError.getValue().getId(),
                        updatedEventOrError.getValue());

        if (messageSentOrError.isFailure()) {
            logger.error(ResultErrorMessages.failedToSendMessage);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.internalError(ResultErrorMessages.failedToSendMessage);
        }

        Set<String> deletedImages =
                oldImages.stream()
                        .map(com.eventer.admin.data.model.Image::getName)
                        .filter(
                                name ->
                                        !savedImageEntities.stream()
                                                .map(com.eventer.admin.data.model.Image::getName)
                                                .toList()
                                                .contains(name))
                        .collect(Collectors.toSet());

        if (!deletedImages.isEmpty()) {
            this.imageHostService.deleteAll(deletedImages);
        }

        logger.info("{} updated successfully", Event.class.getSimpleName());

        return updatedEventOrError;
    }

    @Transactional
    @Override
    public Result delete(Long id) {
        logger.info("Attempting to delete event with id {}", id);

        Optional<com.eventer.admin.data.model.Event> foundEvent = this.eventRepository.findById(id);

        if (foundEvent.isEmpty()) {
            logger.error(ResultErrorMessages.eventNotFound);
            return Result.notFound(ResultErrorMessages.eventNotFound);
        }

        try {
            this.eventRepository.delete(foundEvent.get());
        } catch (Exception e) {
            return Result.invalid(e.getMessage());
        }

        Result messageSentOrError = this.sendMessage(MessageStatics.ACTION_DELETED, id, null);

        if (messageSentOrError.isFailure()) {
            logger.error(messageSentOrError.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fromError(messageSentOrError);
        }

        this.imageHostService.deleteAll(
                foundEvent.get().getImages().stream()
                        .map(com.eventer.admin.data.model.Image::getName)
                        .collect(Collectors.toSet()));

        return Result.success();
    }

    @Transactional(readOnly = true)
    @Override
        public Result<Page<Event>> getEvents(Pageable pageable, String searchTerm) {
        logger.info("Attempting to get events");

        Page<com.eventer.admin.data.model.Event> foundEvents =
                this.eventRepository
                        .findAllByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                                searchTerm, searchTerm, pageable);

        Result<Page<Event>> eventsOrError = EventMapper.toDomainPage(foundEvents);

        if (eventsOrError.isFailure()) {
            logger.error(eventsOrError.getMessage());
            return Result.fromError(eventsOrError);
        }

        logger.info("Events found successfully");

        return eventsOrError;
    }

    @Transactional(readOnly = true)
    @Override
    public Result<Set<Event>> getAllEvents() {
        logger.info("Attempting to get all events");

        List<com.eventer.admin.data.model.Event> foundEvents = this.eventRepository.findAll();

        Result<Set<Event>> eventsOrError = EventMapper.toDomainSet(foundEvents);

        logger.info("Events found successfully");

        return eventsOrError;
    }

    private Result sendMessage(String action, Long id, Event event) {
        logger.info(
                "Attempting to send message for {} {} with id {}",
                action,
                Event.class.getSimpleName(),
                id);

        if (!Objects.equals(action, MessageStatics.ACTION_DELETED) && event == null) {
            logger.error("Object is null when not delete");
            return Result.internalError(ResultErrorMessages.failedToSendMessage);
        }

        Message eventMessage =
                new Message(
                        MessageStatics.NAME_ENTITY_UPDATED,
                        Instant.now(),
                        Event.class.getSimpleName(),
                        action,
                        Objects.equals(action, MessageStatics.ACTION_DELETED) ? id : EventMapper.toDTO(event));

        String messagePayload;
        try {
            messagePayload = this.objectMapper.writeValueAsString(eventMessage);
        } catch (JsonProcessingException e) {
            logger.error(
                    "{} with exception {}",
                    ResultErrorMessages.failedToSendMessage,
                    e.getMessage());
            return Result.internalError(ResultErrorMessages.failedToSendMessage);
        }

        this.messageSenderService.sendMessage(
                ApplicationStatics.EVENTER_DATA_MESSAGE_QUEUE, messagePayload);

        logger.info("Message sent successfully");

        return Result.success();
    }
}
