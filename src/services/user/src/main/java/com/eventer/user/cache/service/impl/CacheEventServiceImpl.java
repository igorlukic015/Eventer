package com.eventer.user.cache.service.impl;

import com.eventer.user.cache.data.model.Event;
import com.eventer.user.cache.data.repository.EventRepository;
import com.eventer.user.cache.service.CacheEventService;
import com.eventer.user.cache.web.AdminWebClient;
import com.eventer.user.cache.web.dto.EventDTO;
import com.eventer.user.utils.ResultErrorMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CacheEventServiceImpl implements CacheEventService {
    private final String eventUri = "/api/v1/event/get-all";
    private final AdminWebClient adminWebClient;
    private final EventRepository eventRepository;
    private static final Logger logger = LoggerFactory.getLogger(CacheEventCategoryServiceImpl.class);

    public CacheEventServiceImpl(AdminWebClient adminWebClient, EventRepository eventRepository) {
        this.adminWebClient = adminWebClient;

        this.eventRepository = eventRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Event> getEvents(final Pageable pageable) {
        logger.info("Attempting to get events");

        Page<Event> allEvents = this.eventRepository.findAll(pageable);

        logger.info("Events found successfully");

        return allEvents;
    }

    @Transactional
    @Override
    public void remove(Long deletedId) {
        logger.info("Attempting to remove event");

        Optional<Event> foundEvent = this.eventRepository.findByEventId(deletedId);

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
