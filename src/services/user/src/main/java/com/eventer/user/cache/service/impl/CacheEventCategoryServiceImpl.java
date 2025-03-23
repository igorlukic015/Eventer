package com.eventer.user.cache.service.impl;

import com.eventer.user.cache.data.model.EventCategory;
import com.eventer.user.cache.data.repository.EventCategoryRepository;
import com.eventer.user.cache.service.CacheEventCategoryService;
import com.eventer.user.cache.web.AdminWebClient;
import com.eventer.user.cache.web.dto.EventCategoryDTO;
import com.eventer.user.utils.ResultErrorMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CacheEventCategoryServiceImpl implements CacheEventCategoryService {
    private final String eventCategoryUri = "/api/v1/event-category/get-all";
    private final AdminWebClient adminWebClient;
    private final EventCategoryRepository eventCategoryRepository;
    private static final Logger logger = LoggerFactory.getLogger(CacheEventCategoryServiceImpl.class);

    public CacheEventCategoryServiceImpl(
            AdminWebClient adminWebClient, EventCategoryRepository eventCategoryRepository) {
        this.adminWebClient = adminWebClient;
        this.eventCategoryRepository = eventCategoryRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Set<EventCategory> getAll() {
        List<EventCategory> allCategories = this.eventCategoryRepository.findAll();

        return new HashSet<>(allCategories);
    }


    @Transactional
    @Override
    public void remove(Long deletedId) {
        logger.info("Attempting to remove category");

        Optional<EventCategory> foundCategory = this.getAll().stream().filter(e -> Objects.equals(e.getCategoryId(), deletedId)).findFirst();

        if (foundCategory.isEmpty()) {
            logger.error("CATEGORY_NOT_FOUND");
            return;
        }

        this.eventCategoryRepository.delete(foundCategory.get());
    }

    @Transactional
    @Override
    public void add(EventCategory newCategory) {

        newCategory.setId(null);
        logger.info("Attempting to add new category");

        this.eventCategoryRepository.save(newCategory);
    }

    @Transactional
    @Override
    public void update(EventCategory updatedCategory) {
        logger.info("Attempting to update category");

        Optional<EventCategory> foundCategory = this.getAll().stream().filter(e -> Objects.equals(e.getCategoryId(), updatedCategory.getCategoryId())).findFirst();

        if (foundCategory.isEmpty()) {
            logger.error("CATEGORY_NOT_FOUND");
            return;
        }

//        this.eventCategoryRepository.delete(foundCategory.get());

        updatedCategory.setId(foundCategory.get().getId());

        this.eventCategoryRepository.save(updatedCategory);
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
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
