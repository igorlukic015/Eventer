package com.eventer.user.web.v1;

import com.eventer.user.cache.service.CacheEventCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/event-category")
public class EventCategoryController {
    private final CacheEventCategoryService cacheEventCategoryService;

    public EventCategoryController(CacheEventCategoryService cacheEventCategoryService) {
        this.cacheEventCategoryService = cacheEventCategoryService;
    }

    @GetMapping
    public ResponseEntity<?> getCategories() {
        return ResponseEntity.ok(this.cacheEventCategoryService.getAll());
    }
}
