package com.eventer.user.service.impl;

import com.eventer.user.data.model.document.EventCategoryRepository;
import com.eventer.user.security.service.JwtService;
import com.eventer.user.service.EventCategoryService;
import com.eventer.user.service.domain.EventCategory;
import com.eventer.user.web.dto.EventCategoryDTO;
import com.github.cigor99.resulter.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventCategoryServiceImpl implements EventCategoryService {
    @Value("${admin-url}")
    private String adminUrl;

    private final JwtService jwtService;
    private final String eventCategoryUri = "/api/v1/event-category/get-all";
    private final EventCategoryRepository eventCategoryRepository;
    private static final Logger logger = LoggerFactory.getLogger(EventCategoryServiceImpl.class);

    public EventCategoryServiceImpl(
            JwtService jwtService, EventCategoryRepository eventCategoryRepository) {
        this.jwtService = jwtService;
        this.eventCategoryRepository = eventCategoryRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void reloadCategories() {
        String token = this.jwtService.generateServiceToken();

        WebClient.builder()
                .baseUrl(this.adminUrl)
                .build()
                .get()
                .uri(eventCategoryUri)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", token))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<EventCategoryDTO>>() {})
                .doOnSuccess(this::saveCategories)
                .doOnError(error -> logger.error(error.getMessage()))
                .subscribe();
    }

    @Override
    public void test() {

        List<com.eventer.user.data.model.document.EventCategory> getCategs = this.eventCategoryRepository.findAll();
        logger.info(
                String.join(
                        ",",
                        getCategs.stream()
                                .map(com.eventer.user.data.model.document.EventCategory::getId)
                                .map(Objects::toString)
                                .toList()));

    }

    private void saveCategories(List<EventCategoryDTO> dtos) {
        Result<Set<EventCategory>> categoriesOrError =
                Result.getResultValueSet(
                        dtos.stream()
                                .map(
                                        dto ->
                                                EventCategory.create(
                                                        dto.id(), dto.name(), dto.description()))
                                .collect(Collectors.toSet()));

        if (categoriesOrError.isFailure()) {
            logger.error(categoriesOrError.getMessage());
            return;
        }

        var categoriesToSave =
                categoriesOrError.getValue().stream()
                        .map(
                                category -> {
                                    com.eventer.user.data.model.document.EventCategory newCategory =
                                            new com.eventer.user.data.model.document
                                                    .EventCategory();
                                    newCategory.setId(category.getId());
                                    newCategory.setName(category.getName());
                                    newCategory.setDescription(category.getDescription());
                                    return newCategory;
                                })
                        .toList();

        this.eventCategoryRepository.saveAll(categoriesToSave);

        logger.info(
                String.join(
                        ",",
                        categoriesOrError.getValue().stream()
                                .map(EventCategory::getId)
                                .map(Objects::toString)
                                .toList()));
    }
}
