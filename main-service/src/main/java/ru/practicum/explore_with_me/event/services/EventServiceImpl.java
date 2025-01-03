package ru.practicum.explore_with_me.event.services;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.StatClient;
import ru.practicum.explore_with_me.category.models.Category;
import ru.practicum.explore_with_me.category.services.CategoryService;
import ru.practicum.explore_with_me.event.mappers.EventMapper;
import ru.practicum.explore_with_me.event.models.Event;
import ru.practicum.explore_with_me.event.models.State;
import ru.practicum.explore_with_me.event.models.StateAction;
import ru.practicum.explore_with_me.event.models.dto.*;
import ru.practicum.explore_with_me.event.repositories.EventRepository;
import ru.practicum.explore_with_me.exceptions.BadRequestException;
import ru.practicum.explore_with_me.exceptions.ConflictException;
import ru.practicum.explore_with_me.exceptions.NotFoundException;
import ru.practicum.explore_with_me.models.ViewStats;
import ru.practicum.explore_with_me.requests.mappers.RequestsMapper;
import ru.practicum.explore_with_me.requests.model.ParticipationRequest;
import ru.practicum.explore_with_me.requests.model.Status;
import ru.practicum.explore_with_me.requests.model.dto.ParticipationRequestDto;
import ru.practicum.explore_with_me.requests.repositories.RequestsRepository;
import ru.practicum.explore_with_me.user.models.User;
import ru.practicum.explore_with_me.user.services.UserService;
import ru.practicum.explore_with_me.util.Util;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.explore_with_me.event.models.QEvent.event;
import static ru.practicum.explore_with_me.util.Util.DATE_FORMATTER;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final CategoryService categoryService;
    private final UserService userService;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final RequestsRepository requestsRepository;
    private final RequestsMapper requestsMapper;
    private final StatClient statClient;

    @Override
    @Transactional
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        log.info("Adding new event: {}", newEventDto);
        User user = userService.getUserById(userId);
        Category category = categoryService.getCategoryById(Long.valueOf(newEventDto.getCategory()));
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("The date and time for which the event is scheduled cannot be earlier than two hours from the current moment");
        }
        String localDateTimeNow = LocalDateTime.now().format(DATE_FORMATTER);
        LocalDateTime localDateTime = LocalDateTime.parse(localDateTimeNow, DATE_FORMATTER);
        Event event = eventMapper.toEvent(newEventDto);
        event.setCategory(category);
        event.setConfirmedRequests(0);
        event.setCreatedOn(localDateTime);
        event.setInitiator(user);
        event.setPublishedOn(localDateTime);
        event.setState(State.PENDING);
        Event savedEvent = eventRepository.save(event);
        log.info("Added new event: {}", event);
        return eventMapper.toEventFullDto(savedEvent);
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long eventId, UpdateEventRequest updateEvent) {
        Event event = getEventById(eventId);
        validUpdate(event, updateEvent);
        Event savedEvent = eventRepository.save(event);
        log.info("Saved event: {}", savedEvent);
        return eventMapper.toEventFullDto(savedEvent);
    }

    @Override
    public List<EventFullDto> getAdminEvents(List<Long> users,
                                             List<String> states,
                                             List<Long> categories,
                                             LocalDateTime rangeStart,
                                             LocalDateTime rangeEnd,
                                             Integer from,
                                             Integer size) {
        log.info("Getting admin events: {}," +
                " states: {}," +
                " categories: {}," +
                " rangeStart: {}," +
                " rangeEnd: {}," +
                " from: {}," +
                " size: {}", users, states, categories, rangeStart, rangeEnd, from, size);
        if (rangeStart == null) {
            rangeStart = Util.DATE_TIME_MIN;
        }
        if (rangeEnd == null) {
            rangeEnd = Util.DATE_TIME_MAX;
        }
        List<State> statesList = null;
        if (states != null) {
            statesList = states.stream().map(String::toUpperCase).map(State::valueOf).toList();
        }
        List<Event> events = eventRepository
                .getAdminEvents(users, statesList, categories, rangeStart, rangeEnd, from, size);
        setConfirmedRequestsList(events);
        setViews(events);
        log.info("Found admin {} events", events.size());
        return eventMapper.toEventFullDtoList(events);
    }

    public List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size) {
        log.info("Adding events by user: {}, from: {}, size: {}", userId, from, size);
        List<Event> eventList = eventRepository.getEventsByUserId(userId, from, size);
        log.info("Found {} events", eventList.size());
        return eventMapper.toEventShortDtoList(eventList);
    }

    @Override
    public List<EventShortDto> getAllEvents(String text,
                                            List<Long> categories,
                                            Boolean paid,
                                            LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd,
                                            Boolean onlyAvailable,
                                            String sort,
                                            Integer from,
                                            Integer size) {
        log.info("Get all public events with text:{}," +
                " categories:{}," +
                " paid:{}," +
                " rangeStart:{}," +
                " rangeEnd:{}," +
                " onlyAvailable:{}," +
                " sort:{}," +
                " from:{}," +
                " size:{}", text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        if (rangeStart == null) {
            rangeStart = Util.DATE_TIME_MIN;
        }
        if (rangeEnd == null) {
            rangeEnd = Util.DATE_TIME_MAX;
        }
        if (rangeEnd.isBefore(rangeStart)) {
            throw new BadRequestException("The start time must be earlier than the end.");
        }
        Pageable pageable;
        switch (sort) {
            case "EVENT_DATE" -> pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "eventDate"));
            case "VIEWS" -> pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "id"));
            case null -> pageable = PageRequest.of(from, size, Sort.unsorted());
            default -> throw new IllegalStateException("Unexpected value: " + sort);
        }
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(event.state.eq(State.PUBLISHED));
        if (text != null) {
            builder.and(event.annotation.containsIgnoreCase(text))
                    .or(event.description.containsIgnoreCase(text));
        }
        if (!categories.isEmpty()) {
            builder.and(event.category.id.in(categories));
        }
        if (paid != null) {
            builder.and(event.paid.eq(paid));
        }
        builder.and(event.eventDate.between(rangeStart, rangeEnd));
        if (onlyAvailable) {
            builder.and(event.participantLimit.eq(0))
                    .or(event.confirmedRequests.lt(event.participantLimit));
        }
        Page<Event> eventList;
        if (builder.getValue() != null) {
            eventList = eventRepository.findAll(builder, pageable);
        } else {
            eventList = eventRepository.findAll(pageable);
        }
        List<Event> events = eventList.getContent();
        setConfirmedRequestsList(events);
        setViews(events);
        log.info("Found public {} events", events.size());
        return eventMapper.toEventShortDtoList(events);
    }

    @Override
    public EventFullDto getEventFullDtoById(Long eventId) {
        log.info("Getting event with id: {}", eventId);
        Event event = eventRepository.getPublishedEventById(eventId, State.PUBLISHED);
        if (event == null) {
            throw new NotFoundException("Event with id " + eventId + " not found");
        }
        List<ViewStats> viewStats = statClient
                .getStats(event.getCreatedOn(), LocalDateTime.now(), List.of("/events/" + eventId), true);
        event.setViews(viewStats.getFirst().getHits());
        log.info("Found event: {}", event);
        return eventMapper.toEventFullDto(event);
    }

    public EventFullDto getEventByIdAndUserId(Long userId, Long eventId) {
        log.info("Getting event: eventId: {}, userId: {}", eventId, userId);
        Event event = eventRepository.getEventByIdAndUserId(userId, eventId);
        if (event == null) {
            throw new NotFoundException("Event with id " + eventId + " not found");
        }
        log.info("Found event by id and user id: {}", event);
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequests(Long userId, Long eventId) {
        log.info("Get participation requests for event with id: {}", eventId);
        List<ParticipationRequest> participationRequestList = requestsRepository.findRequestsByEvent(userId, eventId);
        log.info("Found {} participation requests", participationRequestList.size());
        return requestsMapper.toParticipationRequestDtoList(participationRequestList);
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventRequest updateEventUserRequest) {
        Event event = getEventById(eventId);
        if (event.getState() == State.PUBLISHED) {
            throw new ConflictException("The request status has been reached");
        }
        validUpdate(event, updateEventUserRequest);
        Event savedEvent = eventRepository.save(event);
        log.info("Saved updatedEvent: {}", savedEvent);
        return eventMapper.toEventFullDto(savedEvent);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult eventRequestStatusUpdateResult(Long userId,
                                                                         Long eventId,
                                                                         EventRequestStatusUpdateRequest updateRequest) {
        Event event = getEventById(eventId);
        List<ParticipationRequest> participationRequestList = requestsRepository.findByStatusAndEventId(Status.CONFIRMED, eventId);

        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= participationRequestList.size()) {
            throw new ConflictException("The limit on applications for this event has been reached");
        }


        List<Long> ids = updateRequest.getRequestIds();
        if ((event.getParticipantLimit() + ids.size()) <= requestsRepository.countByIdAndStatus(eventId, Status.PENDING)) {
            throw new ConflictException("The request limit has been reached");
        }
        List<ParticipationRequest> participationRequestList1 = requestsRepository.findRequestsByEventAndIds(userId, eventId, ids);
        for (ParticipationRequest participationRequest : participationRequestList1) {
            if (!participationRequest.getStatus().equals(Status.PENDING)) {
                throw new ConflictException("The request status has been reached");
            }
            participationRequest.setStatus(updateRequest.getStatus());
            requestsRepository.save(participationRequest);
        }
        if (event.getParticipantLimit().equals(requestsRepository.countByIdAndStatus(eventId, Status.CONFIRMED))) {
            List<ParticipationRequest> participationRequests = requestsRepository.findByIdAndStatus(eventId, Status.PENDING);
            for (ParticipationRequest participationRequest : participationRequests) {
                participationRequest.setStatus(Status.REJECTED);
                requestsRepository.save(participationRequest);
            }
        }

        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();
        eventRequestStatusUpdateResult.setConfirmedRequests(requestsMapper
                .toParticipationRequestDtoList(requestsRepository.findByStatusAndEventId(Status.CONFIRMED, eventId)));
        eventRequestStatusUpdateResult.setRejectedRequests(requestsMapper
                .toParticipationRequestDtoList(requestsRepository.findByStatusAndEventId(Status.REJECTED, eventId)));
        log.info("Result: {}", eventRequestStatusUpdateResult);
        return eventRequestStatusUpdateResult;
    }

    @Override
    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id " + eventId + " not found"));
    }

    void setConfirmedRequestsList(List<Event> events) {
        events.forEach(event -> event.setConfirmedRequests(requestsRepository
                .countByStatusAndEventId(Status.CONFIRMED, event.getId())));
    }

    void setViews(List<Event> events) {
        List<String> uris = events.stream().map(event -> "/events/" + event.getId()).toList();
        List<ViewStats> viewStatsList = statClient
                .getStats(LocalDateTime.now().minusYears(20), LocalDateTime.now(), uris, true);
        if (viewStatsList != null) {
            Map<String, Long> uriAndHit = viewStatsList.stream()
                    .collect(Collectors.toMap(ViewStats::getUri, ViewStats::getHits));
            events.forEach(event -> event.setViews(uriAndHit
                    .getOrDefault("/events/" + event.getId(), 0L)));
        }
    }

    private void validUpdate(Event event, UpdateEventRequest updatedEvent) {
        if (updatedEvent.getAnnotation() != null) {
            event.setAnnotation(updatedEvent.getAnnotation());
        }
        if (updatedEvent.getCategory() != null) {
            Category category = categoryService.getCategoryById(updatedEvent.getCategory());
            event.setCategory(category);
        }
        if (updatedEvent.getDescription() != null) {
            event.setDescription(updatedEvent.getDescription());
        }
        if (updatedEvent.getEventDate() != null) {
            if (updatedEvent.getEventDate().isAfter(LocalDateTime.now().plusHours(1))) {
                event.setEventDate(updatedEvent.getEventDate());
            } else {
                throw new BadRequestException("The date and time for which the event is scheduled cannot be earlier than one hours from the current moment");
            }
        }
        if (updatedEvent.getLocation() != null) {
            event.setLocation(updatedEvent.getLocation());
        }
        if (updatedEvent.getPaid() != null) {
            event.setPaid(updatedEvent.getPaid());
        }
        if (updatedEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updatedEvent.getParticipantLimit());
        }
        if (updatedEvent.getRequestModeration() != null) {
            event.setRequestModeration(updatedEvent.getRequestModeration());
        }
        if (updatedEvent.getStateAction() != null) {
            if (updatedEvent.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
                if (event.getState().equals(State.PENDING)) {
                    event.setState(State.PUBLISHED);
                } else {
                    throw new ConflictException("Cannot publish the event because it's not in the right state: " + event.getState().name());
                }
            }
            if (updatedEvent.getStateAction().equals(StateAction.REJECT_EVENT)) {
                if (!event.getState().equals(State.PUBLISHED)) {
                    event.setState(State.CANCELED);
                } else {
                    throw new ConflictException("The event cannot be canceled because it is in the wrong state: " + event.getState().name());
                }
            }
            if (updatedEvent.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
                if (!event.getState().equals(State.PUBLISHED)) {
                    event.setState(State.PENDING);
                } else {
                    throw new ConflictException("Cannot publish the event because it's not in the right state: " + event.getState().name());
                }
            }
            if (updatedEvent.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
                if (!event.getState().equals(State.PUBLISHED)) {
                    event.setState(State.CANCELED);
                } else {
                    throw new ConflictException("The event cannot be canceled because it is in the wrong state: " + event.getState().name());
                }
            }
        }
        if (updatedEvent.getTitle() != null) {
            event.setTitle(updatedEvent.getTitle());
        }
    }
}