package com.eventer.user.web.v1;

import com.eventer.user.cache.service.CacheEventService;
import com.github.igorlukic015.resulter.ResultUnwrapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/event")
public class EventController implements ResultUnwrapper {
    private final CacheEventService cacheEventService;

    public EventController(CacheEventService cacheEventService) {
        this.cacheEventService = cacheEventService;
    }

    @GetMapping
    public ResponseEntity<?> getEvents(
            final Pageable pageable,
            @RequestParam("searchTerm") Optional<String> searchTerm,
            @RequestParam("conditions") Optional<String> conditions,
            @RequestParam("categories") Optional<String> categories) {
        return ResponseEntity.ok(
                this.cacheEventService.getEvents(
                        pageable,
                        searchTerm.orElse(""),
                        conditions.orElse(""),
                        categories.orElse("")));
    }
}
