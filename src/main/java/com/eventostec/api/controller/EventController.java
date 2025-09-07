package com.eventostec.api.controller;

import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventRequestDto;
import com.eventostec.api.domain.event.EventResponseDto;
import com.eventostec.api.service.EventService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/event")
@Tag(name = "Event", description = "Gerenciamento de eventos")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Event> create(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam Long date,
            @RequestParam String city,
            @RequestParam String state,
            @RequestParam Boolean remote,
            @RequestParam String eventUrl,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        EventRequestDto dto = new EventRequestDto(title, description, date, city, state, remote, eventUrl,  image);
        Event newEvent = this.eventService.createEvent(dto);
        return ResponseEntity.ok(newEvent);
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getEvents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        List<EventResponseDto> allEvents = this.eventService.getEvents(page, size);
        return ResponseEntity.ok(allEvents);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<EventResponseDto>> getUpcomingEvents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        List<EventResponseDto> allEvents = this.eventService.getUpcomingEvents(page, size);
        return ResponseEntity.ok(allEvents);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<EventResponseDto>> filtersEvents(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "5") int size,
                                                                @RequestParam(required = false) String title,
                                                                @RequestParam(required = false) String city,
                                                                @RequestParam(required = false) String uf,
                                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                                @RequestParam(required =  false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate
    ) {
        List<EventResponseDto> events = eventService.getFiltersEvents(page, size, title, city, uf, startDate, endDate);
        return ResponseEntity.ok(events);
    }

}

