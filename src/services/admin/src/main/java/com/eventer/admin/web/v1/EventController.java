package com.eventer.admin.web.v1;

import com.eventer.admin.service.EventService;
import com.eventer.admin.service.domain.Event;
import com.eventer.admin.utils.Result;
import com.eventer.admin.web.ControllerBase;
import com.eventer.admin.web.dto.event.CreateEventDTO;
import com.eventer.admin.mapper.EventMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/event")
public class EventController extends ControllerBase {
    private final EventService eventService;
    private final ObjectMapper objectMapper;

    public EventController(EventService eventService, ObjectMapper objectMapper) {
        this.eventService = eventService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(
            @RequestParam("data") String data, @RequestParam("images") List<MultipartFile> images) {
        try {
            CreateEventDTO dto = this.objectMapper.readValue(data, CreateEventDTO.class);
            Result<Event> result = this.eventService.create(EventMapper.toRequest(dto));
            return this.okOrError(result, EventMapper::toDTO);
        } catch (JsonProcessingException e) {
            return this.okOrError(Result.invalid("INVALID_FORM_DATA"), EventMapper::toDTO);
        }
    }

    @GetMapping
    public ResponseEntity<?> getEvents(final Pageable pageable) {
        Result<Page<Event>> result = this.eventService.getEvents(pageable);
        return this.okOrError(result, EventMapper::toDTOs);
    }
}
