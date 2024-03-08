package com.eventer.user.web;

import com.eventer.user.cache.data.model.Event;
import com.eventer.user.cache.data.model.EventCategory;
import com.eventer.user.cache.service.EventCategoryService;
import com.eventer.user.cache.service.EventService;
import com.eventer.user.cache.web.dto.EventCategoryDTO;
import com.eventer.user.cache.web.dto.EventDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {
    private final EventCategoryService eventCategoryService;
    private final EventService eventService;

    public TestController(EventCategoryService eventCategoryService, EventService eventService) {
        this.eventCategoryService = eventCategoryService;
        this.eventService = eventService;
    }

    @GetMapping("/load-categories")
    public ResponseEntity<?> loadCategories() {
        this.eventCategoryService.reloadCategories();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/load-events")
    public ResponseEntity<?> loadEvents() {
        this.eventService.reloadEvents();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/list-all-c")
    public ResponseEntity<?> listAllCategories() {
        Set<EventCategory> allCategories = this.eventCategoryService.getAll();

        Set<EventCategoryDTO> result =
                allCategories.stream()
                        .map(
                                category ->
                                        new EventCategoryDTO(
                                                category.getId(),
                                                category.getName(),
                                                category.getDescription()))
                        .collect(Collectors.toSet());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/list-all-e")
    public ResponseEntity<?> listAllEvents(Pageable pageable) {
        Page<Event> eventsPage = this.eventService.getEvents(pageable);

        List<EventDTO> result =
                eventsPage.stream()
                        .map(
                                event ->
                                        (new EventDTO(
                                                event.getId(),
                                                event.getTitle(),
                                                event.getDescription(),
                                                event.getLocation(),
                                                new HashSet<>(),
                                                new HashSet<>(),
                                                new HashSet<>())))
                        .toList();

        return ResponseEntity.ok(result);
    }
}
