package com.eventer.user.cache.service.impl;

import com.eventer.user.cache.data.model.EventCategory;
import com.eventer.user.cache.data.repository.EventCategoryRepository;
import com.eventer.user.cache.service.EventCategoryService;
import com.eventer.user.cache.web.AdminWebClient;
import com.eventer.user.cache.web.dto.EventCategoryDTO;
import com.eventer.user.utils.ResultErrorMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Service
public class EventCategoryServiceImpl implements EventCategoryService {
    private final String eventCategoryUri = "/api/v1/event-category/get-all";
    private final AdminWebClient adminWebClient;
    private final EventCategoryRepository eventCategoryRepository;
    private static final Logger logger = LoggerFactory.getLogger(EventCategoryServiceImpl.class);

    public EventCategoryServiceImpl(
            AdminWebClient adminWebClient, EventCategoryRepository eventCategoryRepository) {
        this.adminWebClient = adminWebClient;
        this.eventCategoryRepository = eventCategoryRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Set<EventCategory> getAll() {
        List<EventCategory> allCategories =
                this.eventCategoryRepository.findAll();

        return new HashSet<>(allCategories);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public void reloadCategories() {
        this.adminWebClient
                .loadEntityClient(eventCategoryUri)
                .bodyToMono(new ParameterizedTypeReference<List<EventCategoryDTO>>() {})
                .doOnSuccess(this::saveCategories)
                .doOnError(error -> logger.error(error.getMessage()))
                .subscribe();
    }

    private void saveCategories(List<EventCategoryDTO> data) {
        if (data == null || data.isEmpty()) {
            logger.error(ResultErrorMessages.categoriesNotReceived);
            return;
        }

        this.eventCategoryRepository.deleteAll();

        var categoriesToSave =
                data.stream()
                        .map(dto -> new EventCategory(dto.id(), dto.name(), dto.description()))
                        .toList();

        this.eventCategoryRepository.saveAll(categoriesToSave);

        logger.info("Categories saved to cache");
    }
}
