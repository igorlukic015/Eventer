package com.eventer.admin.api;

import com.eventer.admin.common.dto.EventCategoryDTO;
import com.eventer.admin.model.EventCategory;
import com.eventer.admin.service.EventCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/event-category")
public class EventCategoryController {

    private final EventCategoryService eventCategoryService;

    public EventCategoryController(EventCategoryService eventCategoryService) {
        this.eventCategoryService = eventCategoryService;
    }

    @PostMapping
    public ResponseEntity<EventCategory> create(@RequestBody EventCategoryDTO dto) {
        var result = this.eventCategoryService.create(dto);
        return ResponseEntity.ok(result.get());
    }
}
