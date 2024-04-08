package com.eventer.admin.web.v1;

import com.eventer.admin.service.domain.EventCategory;
import com.eventer.admin.web.dto.eventcategory.EventCategoryDTO;
import com.eventer.admin.mapper.EventCategoryMapper;
import com.eventer.admin.service.EventCategoryService;
import com.github.igorlukic015.resulter.Result;
import com.github.igorlukic015.resulter.ResultUnwrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/event-category")
public class EventCategoryController implements ResultUnwrapper {

    private final EventCategoryService eventCategoryService;

    public EventCategoryController(EventCategoryService eventCategoryService) {
        this.eventCategoryService = eventCategoryService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody EventCategoryDTO dto) {
        Result<EventCategory> result =
                this.eventCategoryService.create(EventCategoryMapper.toRequest(dto));
        return this.okOrError(result, EventCategoryMapper::toDTO);
    }

    @GetMapping
    public ResponseEntity<?> getCategories(final Pageable pageable, @RequestParam("searchTerm") Optional<String> searchTerm) {
        Result<Page<EventCategory>> result = this.eventCategoryService.getCategories(pageable, searchTerm.orElse(""));
        return this.okOrError(result, EventCategoryMapper::toDTOs);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllCategories() {
        Result<Set<EventCategory>> result = this.eventCategoryService.getAllCategories();
        return this.okOrError(result, EventCategoryMapper::toDTOs);
    }
}
