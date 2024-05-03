package com.eventer.admin.web.v1;

import com.eventer.admin.service.EventService;
import com.eventer.admin.service.ImageHostService;
import com.eventer.admin.service.domain.Event;
import com.eventer.admin.web.dto.event.UpdateEventDTO;
import com.github.igorlukic015.resulter.Result;
import com.eventer.admin.web.dto.event.CreateEventDTO;
import com.eventer.admin.mapper.EventMapper;
import com.github.igorlukic015.resulter.ResultUnwrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/event")
public class EventController implements ResultUnwrapper {
    private final EventService eventService;
    private final ImageHostService imageHostService;

    public EventController(
            EventService eventService,
            ImageHostService imageHostService) {
        this.eventService = eventService;
        this.imageHostService = imageHostService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(@RequestParam("images") List<MultipartFile> images) {
        Set<String> response = this.imageHostService.saveAllImages(images);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateEventDTO dto) {
        Result<Event> result = this.eventService.create(EventMapper.toRequest(dto));
        return this.okOrError(result, EventMapper::toDTO);
    }

    @GetMapping
    public ResponseEntity<?> getEvents(final Pageable pageable) {
        Result<Page<Event>> result = this.eventService.getEvents(pageable);
        return this.okOrError(result, EventMapper::toDTOPage);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllEvents() {
        Result<Set<Event>> result = this.eventService.getAllEvents();
        return this.okOrError(result, EventMapper::toDTOs);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateEventDTO dto) {
        Result<Event> result = this.eventService.update(EventMapper.toRequest(dto));
        return this.okOrError(result, EventMapper::toDTO);
    }
}
