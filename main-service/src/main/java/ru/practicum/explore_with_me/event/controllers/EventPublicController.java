package ru.practicum.explore_with_me.event.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.StatClient;
import ru.practicum.explore_with_me.event.EventService;
import ru.practicum.explore_with_me.event.models.dto.EventFullDto;
import ru.practicum.explore_with_me.event.models.dto.EventShortDto;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explore_with_me.util.Util.*;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventPublicController {
    private final EventService eventService;
    private final StatClient statClient;

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable Long id, HttpServletRequest request) {
        log.info("Start of endpoint processing (get) /events/{}", id);
        statClient.saveHit("ewm-main-service", request);
        return eventService.getEventById(id);
    }

    @GetMapping
    public List<EventShortDto> getAllEvents(@RequestParam(required = false) String text,
                                            @RequestParam(required = false) List<Long> categories,
                                            @RequestParam(required = false) Boolean paid,
                                            @RequestParam(required = false) String rangeStart,
                                            @RequestParam(required = false) String rangeEnd,
                                            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                            @RequestParam(defaultValue = "EVENT_DATE") String sort,
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size,
                                            HttpServletRequest request) {
        log.info("Start of endpoint processing (get) /events?");
        LocalDateTime start = DATE_TIME_MIN;
        LocalDateTime end = DATE_TIME_MAX;
        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, DATE_FORMATTER);
        }
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, DATE_FORMATTER);
        }
        return eventService.getAllEvents(text,
                categories,
                paid,
                start,
                end,
                onlyAvailable,
                sort,
                from,
                size);
    }
}
