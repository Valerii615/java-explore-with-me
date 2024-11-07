package ru.practicum.explore_with_me.event.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explore_with_me.event.models.Event;
import ru.practicum.explore_with_me.event.models.Location;
import ru.practicum.explore_with_me.event.models.dto.EventFullDto;
import ru.practicum.explore_with_me.event.models.dto.EventShortDto;
import ru.practicum.explore_with_me.event.models.dto.LocationDto;
import ru.practicum.explore_with_me.event.models.dto.NewEventDto;

import java.util.List;

@Mapper
public interface EventMapper {

    @Mapping(target = "category", ignore = true)
    Event toEvent(NewEventDto newEventDto);

    EventFullDto toEventFullDto(Event event);

    EventShortDto toEventShortDto(Event event);

    List<EventShortDto> toEventShortDtoList(List<Event> events);

    List<EventFullDto> toEventFullDtoList(List<Event> events);

    LocationDto toDto(Location location);
}
