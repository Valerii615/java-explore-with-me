package ru.practicum.explore_with_me;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.mapper.EndpointMapper;
import ru.practicum.explore_with_me.models.Endpoint;
import ru.practicum.explore_with_me.models.EndpointHit;
import ru.practicum.explore_with_me.models.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StetsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final EndpointMapper endpointMapper;

    @Override
    @Transactional
    public void addEndpoint(EndpointHit endpointHit) {
        log.info("Adding endpoint: {}", endpointHit);
        Endpoint endpoint = statsRepository.save(endpointMapper.toEndpoint(endpointHit));
        log.info("Added endpoint: {}", endpoint);
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime startTime, LocalDateTime endTime, List<String> uris, Boolean unique) {
        log.info("Start getting stats list");
        if (startTime.isAfter(endTime)) {
            throw new BadRequestException("Start time is after end time");
        }
        List<ViewStats> viewStats;
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                log.info("Getting a list stats (startTime: {}, endTime: {})(uris == null || uris.isEmpty())(unique = true))", startTime, endTime);
                viewStats = statsRepository.findViewStatsByStartTimeAndEndTimeBetweenUnique(startTime, endTime);
            } else {
                log.info("Getting a list stats (startTime: {}, endTime: {})(uris == null || uris.isEmpty())(unique = false))", startTime, endTime);
                viewStats = statsRepository.findViewStatsByStartTimeAndEndTimeBetween(startTime, endTime);
            }
        } else {
            if (unique) {
                log.info("Getting a list stats (startTime: {}, endTime: {})(uris: {})(unique = true))", startTime, endTime, uris);
                viewStats = statsRepository.findViewStatsByStartTimeAndEndTimeBetweenUniqueUris(startTime, endTime, uris);
            } else {
                log.info("Getting a list stats (startTime: {}, endTime: {})(uris: {})(unique = false))", startTime, endTime, uris);
                viewStats = statsRepository.findViewStatsByStartTimeAndEndTimeBetweenUris(startTime, endTime, uris);
            }
        }
        log.info("The resulting list: {}", viewStats);
        return viewStats;
    }
}
