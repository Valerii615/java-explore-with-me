package ru.practicum.explore_with_me.requests.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explore_with_me.requests.model.ParticipationRequest;
import ru.practicum.explore_with_me.requests.model.dto.ParticipationRequestDto;

import java.util.List;

@Mapper
public interface RequestsMapper {

    @Mapping(source = "event.id", target = "event")
    @Mapping(source = "requester.id", target = "requester")
    ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest);

    List<ParticipationRequestDto> toParticipationRequestDtoList(List<ParticipationRequest> participationRequestList);
}
