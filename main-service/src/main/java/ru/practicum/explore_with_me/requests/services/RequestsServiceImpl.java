package ru.practicum.explore_with_me.requests.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.event.models.Event;
import ru.practicum.explore_with_me.event.models.State;
import ru.practicum.explore_with_me.event.repositories.EventRepository;
import ru.practicum.explore_with_me.exceptions.ConflictException;
import ru.practicum.explore_with_me.exceptions.NotFoundException;
import ru.practicum.explore_with_me.requests.mappers.RequestsMapper;
import ru.practicum.explore_with_me.requests.model.ParticipationRequest;
import ru.practicum.explore_with_me.requests.model.Status;
import ru.practicum.explore_with_me.requests.model.dto.ParticipationRequestDto;
import ru.practicum.explore_with_me.requests.repositories.RequestsRepository;
import ru.practicum.explore_with_me.user.models.User;
import ru.practicum.explore_with_me.user.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explore_with_me.util.Util.DATE_FORMATTER;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestsServiceImpl implements RequestsService {
    private final RequestsRepository requestsRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestsMapper requestsMapper;

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId) {
        log.info("Get requests for {}", userId);
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        List<ParticipationRequest> participationRequestList = requestsRepository.findByRequesterId(userId);
        log.info("Found {} requests", participationRequestList.size());
        return requestsMapper.toParticipationRequestDtoList(participationRequestList);
    }

    @Override
    @Transactional
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        log.info("Adding request userId: {}, eventId: {}", userId, eventId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id " + eventId + " not found"));
        if (event.getInitiator().getId().equals(user.getId())) {
            throw new ConflictException("The initiator of the event cannot add a request to participate in his event");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("The state of the event is not published");
        }
        List<ParticipationRequest> participationRequestList = requestsRepository.findByStatusAndEventId(Status.CONFIRMED, eventId);
        if (event.getParticipantLimit() != 0) {
            if (event.getParticipantLimit() <= participationRequestList.size()) {
                throw new ConflictException("The request limit has been reached");
            }
        }
        String localDateTimeNow = LocalDateTime.now().format(DATE_FORMATTER);
        LocalDateTime localDateTime = LocalDateTime.parse(localDateTimeNow, DATE_FORMATTER);
        ParticipationRequest participationRequest = ParticipationRequest.builder()
                .created(localDateTime)
                .event(event)
                .requester(user)
                .build();
        if (event.getParticipantLimit() != 0 && event.getRequestModeration()) {
            participationRequest.setStatus(Status.PENDING);
        } else {
            participationRequest.setStatus(Status.CONFIRMED);
        }
        participationRequest = requestsRepository.save(participationRequest);
        log.info("Added request: {}", participationRequest);
        return requestsMapper.toParticipationRequestDto(participationRequest);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        log.info("Cancel request userId: {}, requestId: {}", userId, requestId);
        ParticipationRequest participationRequest = requestsRepository.findByIdAndRequesterId(requestId, userId);
        if (participationRequest == null) {
            throw new NotFoundException("Request with id " + requestId + " not found");
        } else {
            participationRequest.setStatus(Status.CANCELED);
        }
        ParticipationRequest participationRequestSaved = requestsRepository.save(participationRequest);
        return requestsMapper.toParticipationRequestDto(participationRequestSaved);
    }
}
