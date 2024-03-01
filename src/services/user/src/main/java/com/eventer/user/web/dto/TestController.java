package com.eventer.user.web.dto;

import com.eventer.user.service.EventCategoryService;
import com.eventer.user.service.impl.EventCategoryServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    private final EventCategoryService eventCategoryService;

    public TestController(EventCategoryService eventCategoryService) {
        this.eventCategoryService = eventCategoryService;
    }

    @GetMapping
    public ResponseEntity<?> test(){
        this.eventCategoryService.reloadCategories();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test2")
    public ResponseEntity<?> test2() {
        this.eventCategoryService.test();
        return ResponseEntity.ok().build();
    }
}
