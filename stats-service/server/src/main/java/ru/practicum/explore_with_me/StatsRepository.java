package ru.practicum.explore_with_me;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore_with_me.models.Endpoint;
import ru.practicum.explore_with_me.models.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Endpoint, Long> {
    @Query("""
            select new ru.practicum.explore_with_me.models.ViewStats(e.app, e.uri, count (e.ip))
            from Endpoint as e
            where e.timestamp between :start and :end
            group by e.app, e.uri
            order by count (e.ip) desc
            """)
    List<ViewStats> findViewStatsByStartTimeAndEndTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query("""
            select new ru.practicum.explore_with_me.models.ViewStats(e.app, e.uri, count (distinct e.ip))
            from Endpoint as e
            where e.timestamp between :start and :end
            group by e.app, e.uri
            order by count (distinct e.ip) desc
            """)
    List<ViewStats> findViewStatsByStartTimeAndEndTimeBetweenUnique(LocalDateTime start, LocalDateTime end);

    @Query("""
            select new ru.practicum.explore_with_me.models.ViewStats(e.app, e.uri, count (e.ip))
            from Endpoint as e
            where e.timestamp between :start and :end
            and e.uri in :uris
            group by e.app, e.uri
            order by count (e.ip) desc
            """)
    List<ViewStats> findViewStatsByStartTimeAndEndTimeBetweenUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("""
            select new ru.practicum.explore_with_me.models.ViewStats(e.app, e.uri, count (distinct e.ip))
            from Endpoint as e
            where e.timestamp between :start and :end
            and e.uri in :uris
            group by e.app, e.uri
            order by count (distinct e.ip) desc
            """)
    List<ViewStats> findViewStatsByStartTimeAndEndTimeBetweenUniqueUris(LocalDateTime start, LocalDateTime end, List<String> uris);
}
