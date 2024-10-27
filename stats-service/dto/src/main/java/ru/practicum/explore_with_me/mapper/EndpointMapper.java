package ru.practicum.explore_with_me.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explore_with_me.models.Endpoint;
import ru.practicum.explore_with_me.models.EndpointHit;

@Mapper
public interface EndpointMapper {
    Endpoint toEndpoint(EndpointHit endpointHit);
}
