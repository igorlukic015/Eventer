package com.eventer.user.cache.service.impl;

import com.eventer.user.cache.data.model.Event;
import com.eventer.user.cache.data.model.Event$;
import com.eventer.user.cache.data.repository.EventCategoryRepository;
import com.eventer.user.cache.data.repository.EventRepository;
import com.eventer.user.cache.service.CacheEventService;
import com.eventer.user.cache.web.AdminWebClient;
import com.eventer.user.cache.web.dto.EventCategoryDTO;
import com.eventer.user.cache.web.dto.EventDTO;
import com.eventer.user.utils.ResultErrorMessages;
import com.redis.om.spring.search.stream.EntityStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CacheEventServiceImpl implements CacheEventService {
    private final String eventUri = "/api/v1/event/get-all";
    private final AdminWebClient adminWebClient;
    private final EventRepository eventRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private static final Logger logger =
            LoggerFactory.getLogger(CacheEventCategoryServiceImpl.class);

    @Autowired EntityStream entityStream;

    public CacheEventServiceImpl(
            AdminWebClient adminWebClient,
            EventRepository eventRepository,
            EventCategoryRepository eventCategoryRepository) {
        this.adminWebClient = adminWebClient;
        this.eventRepository = eventRepository;
        this.eventCategoryRepository = eventCategoryRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Event> getAll() {
        List<Event> allEvents = this.eventRepository.findAll();

        return new HashSet<>(allEvents);
    }

    @Transactional
    @Override
    public void add(Event newEvent) {
        logger.info("Attempting to add new event");

        this.eventRepository.save(newEvent);
    }

    @Transactional
    @Override
    public void update(Event updatedEvent) {
        logger.info("Attempting to update event");

        Optional<Event> foundEvent =
                this.getAll().stream()
                        .filter(e -> Objects.equals(e.getEventId(), updatedEvent.getEventId()))
                        .findFirst();

        if (foundEvent.isEmpty()) {
            logger.error("CATEGORY_NOT_FOUND");
            return;
        }

        //        this.eventCategoryRepository.delete(foundCategory.get());

        updatedEvent.setId(foundEvent.get().getId());

        this.eventRepository.save(updatedEvent);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Event> getEvents(
            final Pageable pageable,
            String searchTerm,
            String conditionsQuery,
            String categoriesQuery) {
        logger.info("Attempting to get events");

        final int start = (int) pageable.getOffset();

        List<Event> allEvents =
                Objects.equals(searchTerm, "")
                        ? entityStream.of(Event.class).skip(start).collect(Collectors.toList())
                        : entityStream
                                .of(Event.class)
                                .skip(start)
                                .filter(
                                        Event$.TITLE
                                                .containing(searchTerm)
                                                .or(Event$.DESCRIPTION.containing(searchTerm)))
                                .collect(Collectors.toList());

        if (StringUtils.hasText(categoriesQuery)) {
            List<Long> categoriesQueryElements =
                    Arrays.stream(categoriesQuery.split(";")).map(Long::valueOf).toList();

            allEvents =
                    allEvents.stream()
                            .filter(
                                    event ->
                                            (event.getCategories().stream()
                                                            .map(EventCategoryDTO::id)
                                                            .collect(Collectors.toSet()))
                                                    .containsAll(categoriesQueryElements))
                            .toList();
        }

        if (StringUtils.hasText(conditionsQuery)) {
            List<String> conditionsQueryElements =
                    Arrays.stream(conditionsQuery.split(";")).map(String::toLowerCase).toList();

            allEvents =
                    allEvents.stream()
                            .filter(
                                    event ->
                                            (event.getWeatherConditions().stream()
                                                            .map(String::toLowerCase)
                                                            .collect(Collectors.toSet()))
                                                    .containsAll(conditionsQueryElements))
                            .toList();
        }

        logger.info("Events found successfully");

        final int end = Math.min((start + pageable.getPageSize()), allEvents.size());

        allEvents = allEvents.stream().skip(start).limit(pageable.getPageSize()).toList();

        return new PageImpl<>(allEvents.subList(start, end), pageable, allEvents.size());
    }

    @Transactional
    @Override
    public void remove(Long deletedId) {
        logger.info("Attempting to remove event");

        Optional<Event> foundEvent = this.eventRepository.findOneByEventId(deletedId);

        if (foundEvent.isEmpty()) {
            logger.error("EVENT_NOT_FOUND");
            return;
        }

        this.eventRepository.delete(foundEvent.get());
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public void reloadEvents() {
        this.adminWebClient
                .loadEntityClient(eventUri)
                .bodyToMono(new ParameterizedTypeReference<List<EventDTO>>() {})
                .doOnSuccess(this::saveEvents)
                .doOnError(error -> logger.error(error.getMessage()))
                .subscribe();
    }

    private void saveEvents(List<EventDTO> data) {
        if (data == null || data.isEmpty()) {
            logger.error(ResultErrorMessages.eventsNotReceived);
            return;
        }

        this.eventRepository.deleteAll();

        Set<Event> events =
                data.stream()
                        .map(
                                dto ->
                                        new Event(
                                                dto.id(),
                                                dto.title(),
                                                dto.description(),
                                                dto.location(),
                                                dto.weatherConditions(),
                                                dto.categories(),
                                                dto.images()))
                        .collect(Collectors.toSet());

        this.eventRepository.saveAll(events);

        logger.info("Events saved to cache");
    }
}
