package ru.practicum.explore_with_me.event.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.event.services.EventService;
import ru.practicum.explore_with_me.event.models.dto.EventFullDto;
import ru.practicum.explore_with_me.event.models.dto.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explore_with_me.util.Util.*;

@Slf4j
@RestController
@RequestMapping("/admin/events")
@AllArgsConstructor
public class EventAdminController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(required = false) List<Long> users,
                                        @RequestParam(required = false) List<String> states,
                                        @RequestParam(required = false) List<Long> categories,
                                        @RequestParam(required = false) String rangeStart,
                                        @RequestParam(required = false) String rangeEnd,
                                        @RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("Start of endpoint processing (get) /admin/events?users={}&states={}&categories={}&rangeStart={}&rangeEnd={}&from={}&size={}", users, states, categories, rangeStart, rangeEnd, from, size);
        LocalDateTime start = DATE_TIME_MIN;
        LocalDateTime end = DATE_TIME_MAX;
        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, DATE_FORMATTER);
        }
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, DATE_FORMATTER);
        }
        return eventService.getAdminEvents(users, states, categories, start, end, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId, @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("Start of endpoint processing (patch) /admin/events/{}", eventId);
        return eventService.updateEvent(eventId, updateEventAdminRequest);
    }
}


