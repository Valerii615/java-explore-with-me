package ru.practicum.explore_with_me.event.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.event.models.dto.*;
import ru.practicum.explore_with_me.event.services.EventService;
import ru.practicum.explore_with_me.requests.model.dto.ParticipationRequestDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/events")
@AllArgsConstructor
public class EventPrivateController {
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Long userId, @RequestBody @Valid NewEventDto newEventDto) {
        log.info("Start of endpoint processing (post) /users/{}/events", userId);
        return eventService.addEvent(userId, newEventDto);
    }

    @GetMapping
    public List<EventShortDto> getEventsByUserId(@PathVariable Long userId,
                                                 @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                 @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Start of endpoint processing (get) /users/{}/events?from={}&size={}", userId, from, size);
        return eventService.getEventsByUserId(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventByIdAndUserId(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Start of endpoint processing (get) /users/{}/events/{}", userId, eventId);
        return eventService.getEventByIdAndUserId(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @RequestBody @Valid UpdateEventRequest updateEventUserRequest) {
        log.info("Start of endpoint processing (patch) /users/{}/events/{}", userId, eventId);
        return eventService.updateEvent(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Start of endpoint processing (get) /users/{}/events/{}/requests", userId, eventId);
        return eventService.getParticipationRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult eventRequestStatusUpdateResult(@PathVariable Long userId,
                                                                         @PathVariable Long eventId,
                                                                         @RequestBody @Valid EventRequestStatusUpdateRequest updateRequest) {
        log.info("Start of endpoint processing (patch) /users/{}/events/{}/requests", userId, eventId);
        return eventService.eventRequestStatusUpdateResult(userId, eventId, updateRequest);
    }


}
