package ru.practicum.explore_with_me;

import ru.practicum.explore_with_me.models.EndpointHit;
import ru.practicum.explore_with_me.models.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    void addEndpoint(EndpointHit endpointHit);

    List<ViewStats> getStats(LocalDateTime startTime, LocalDateTime endTime, List<String> uris, Boolean unique);

}
