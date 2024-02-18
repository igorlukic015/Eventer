package com.eventer.admin.service.impl;

import com.eventer.admin.contracts.event.CreateEventRequest;
import com.eventer.admin.service.EventCategoryService;
import com.eventer.admin.service.ImageService;
import com.eventer.admin.service.domain.Event;
import com.eventer.admin.mapper.EventMapper;
import com.eventer.admin.data.repository.EventRepository;
import com.eventer.admin.service.EventService;
import com.eventer.admin.service.domain.EventCategory;
import com.eventer.admin.service.domain.Image;
import com.eventer.admin.utils.Result;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventCategoryService eventCategoryService;
    private final ImageService imageService;

    public EventServiceImpl(
            EventRepository eventRepository,
            EventCategoryService eventCategoryService,
            ImageService imageService) {
        this.eventRepository = eventRepository;
        this.eventCategoryService = eventCategoryService;
        this.imageService = imageService;
    }

    @Override
    public Result<Event> create(CreateEventRequest createEventRequest) {
        if (createEventRequest.weatherConditionsOrError().isFailure()) {
            this.imageService.deleteOnFailure(createEventRequest.savedImages());
            return Result.fromError(createEventRequest.weatherConditionsOrError());
        }

        if (createEventRequest.eventCategoriesOrError().isFailure()) {
            this.imageService.deleteOnFailure(createEventRequest.savedImages());
            return Result.fromError(createEventRequest.eventCategoriesOrError());
        }

        if (createEventRequest.dateOrError().isFailure()) {
            this.imageService.deleteOnFailure(createEventRequest.savedImages());
            return Result.fromError(createEventRequest.dateOrError());
        }

        Result<Set<EventCategory>> categoriesOrError =
                this.eventCategoryService.getCategoriesByIds(
                        createEventRequest.eventCategoriesOrError().getValue().stream()
                                .map(EventCategory::getId)
                                .collect(Collectors.toSet()));

        if (categoriesOrError.isFailure()) {
            this.imageService.deleteOnFailure(createEventRequest.savedImages());
            return Result.fromError(categoriesOrError);
        }

        Set<Image> savedImages = new HashSet<>();

        for (Path savedImagePath : createEventRequest.savedImages()) {
            String imageName = savedImagePath.toString().replace("\\", "/");

            Result<Image> imageOrError = Image.create(imageName);

            if (imageOrError.isFailure()) {
                this.imageService.deleteOnFailure(createEventRequest.savedImages());
                return Result.invalid("INVALID_IMAGE");
            }

            Result<Image> savedImageOrError =
                    this.imageService.saveImage(
                            imageOrError.getValue(), Event.class.getSimpleName());

            if (savedImageOrError.isFailure()) {
                this.imageService.deleteOnFailure(createEventRequest.savedImages());
                return Result.fromError(savedImageOrError);
            }

            savedImages.add(savedImageOrError.getValue());
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
            this.imageService.deleteOnFailure(createEventRequest.savedImages());
            return Result.fromError(eventOrError);
        }

        com.eventer.admin.data.model.Event event = EventMapper.toModel(eventOrError.getValue(), savedImages);

        com.eventer.admin.data.model.Event result = this.eventRepository.save(event);

        return EventMapper.toDomain(result);
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
}
