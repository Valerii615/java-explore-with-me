package ru.practicum.explore_with_me.event.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.StatClient;
import ru.practicum.explore_with_me.event.models.dto.EventFullDto;
import ru.practicum.explore_with_me.event.models.dto.EventShortDto;
import ru.practicum.explore_with_me.event.services.EventService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explore_with_me.util.Util.DATE_FORMAT;

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
                                            @DateTimeFormat(pattern = DATE_FORMAT)
                                            @RequestParam(required = false) LocalDateTime rangeStart,
                                            @DateTimeFormat(pattern = DATE_FORMAT)
                                            @RequestParam(required = false) LocalDateTime rangeEnd,
                                            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                            @RequestParam(defaultValue = "EVENT_DATE") String sort,
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size,
                                            HttpServletRequest request) {
        log.info("Start of endpoint processing (get) /events?");
        statClient.saveHit("ewm-main-service", request);
        return eventService.getAllEvents(text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                from,
                size);
    }
}
