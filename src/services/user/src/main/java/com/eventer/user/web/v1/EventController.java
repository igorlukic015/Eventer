package com.eventer.user.web.v1;

import com.eventer.user.cache.service.EventService;
import com.github.igorlukic015.resulter.Result;
import com.github.igorlukic015.resulter.ResultUnwrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/event")
public class EventController implements ResultUnwrapper {
//    private final EventService eventService;

    public EventController() {
    }

    @GetMapping
    public ResponseEntity<?> getEvents(final Pageable pageable) {
//        Result<Page<Event>> result = this.eventService.getEvents(pageable);
//        return this.okOrError(result, EventMapper::toDTOPage);

        return ResponseEntity.ok(null);
    }
}
