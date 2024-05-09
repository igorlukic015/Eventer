package com.eventer.user.web.v1;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/event-category")
public class EventCategoryController {
    public EventCategoryController() {
    }

    @GetMapping
    public ResponseEntity<?> getCategories(final Pageable pageable) {
        return ResponseEntity.ok(new ArrayList<>());
    }
}
