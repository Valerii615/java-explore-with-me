package ru.practicum.explore_with_me.requests;

import ru.practicum.explore_with_me.requests.model.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestsService {

    List<ParticipationRequestDto> getRequests(Long userId);

    ParticipationRequestDto addRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
