package com.eventer.admin.web.v1;


import com.eventer.admin.service.EventService;
import com.eventer.admin.service.domain.Event;
import com.eventer.admin.utils.Result;
import com.eventer.admin.web.ControllerBase;
import com.eventer.admin.web.dto.event.CreateEventDTO;
import com.eventer.admin.mapper.EventMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/event")
public class EventController extends ControllerBase {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateEventDTO dto) {
        Result<Event> result = this.eventService.create(EventMapper.toRequest(dto));
        return this.okOrError(result, EventMapper::toDTO);
    }
}
