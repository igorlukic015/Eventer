package com.eventer.admin.web.v1;

import com.eventer.admin.service.EventService;
import com.eventer.admin.service.domain.Event;
import com.eventer.admin.utils.Result;
import com.eventer.admin.web.ControllerBase;
import com.eventer.admin.web.dto.event.CreateEventDTO;
import com.eventer.admin.mapper.EventMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/event")
public class EventController extends ControllerBase {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@ModelAttribute CreateEventDTO dto) {
        Result<Event> result = this.eventService.create(EventMapper.toRequest(dto));
        return this.okOrError(result, EventMapper::toDTO);
    }

    @GetMapping
    public ResponseEntity<?> getEvents(final Pageable pageable) {
        Result<Page<Event>> result = this.eventService.getEvents(pageable);
        return this.okOrError(result, EventMapper::toDTOs);
    }
}
