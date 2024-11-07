package ru.practicum.explore_with_me.event.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.event.models.dto.EventFullDto;
import ru.practicum.explore_with_me.event.models.dto.UpdateEventRequest;
import ru.practicum.explore_with_me.event.services.EventService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explore_with_me.util.Util.DATE_FORMAT;

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
                                        @DateTimeFormat(pattern = DATE_FORMAT)
                                        @RequestParam(required = false) LocalDateTime rangeStart,
                                        @DateTimeFormat(pattern = DATE_FORMAT)
                                        @RequestParam(required = false) LocalDateTime rangeEnd,
                                        @RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("Start of endpoint processing (get) /admin/events?users={}&states={}&categories={}&rangeStart={}&rangeEnd={}&from={}&size={}", users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.getAdminEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId, @RequestBody @Valid UpdateEventRequest updateEventAdminRequest) {
        log.info("Start of endpoint processing (patch) /admin/events/{}", eventId);
        return eventService.updateEvent(eventId, updateEventAdminRequest);
    }
}


