package ru.practicum.explore_with_me.event.services;

import ru.practicum.explore_with_me.event.models.dto.*;
import ru.practicum.explore_with_me.requests.model.dto.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto addEvent(Long userId, NewEventDto newEventDto);

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventFullDto> getAdminEvents(List<Long> users,
                                      List<String> states,
                                      List<Long> categories,
                                      LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd,
                                      Integer from,
                                      Integer size);


    List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size);

    List<EventShortDto> getAllEvents(String text,
                                     List<Long> categories,
                                     Boolean paid,
                                     LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd,
                                     Boolean onlyAvailable,
                                     String sort,
                                     Integer from,
                                     Integer size);

    EventFullDto getEventById(Long eventId);

    EventFullDto getEventByIdAndUserId(Long userId, Long eventId);

    List<ParticipationRequestDto> getParticipationRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult eventRequestStatusUpdateResult(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);
}