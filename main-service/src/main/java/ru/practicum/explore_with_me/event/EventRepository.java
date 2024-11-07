package ru.practicum.explore_with_me.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.explore_with_me.event.models.Event;
import ru.practicum.explore_with_me.event.models.State;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    @Query("""
            select e
            from Event e
            where e.initiator.id = :userId
            order by e.id
            limit :size offset :from
            """)
    List<Event> getEventsByUserId(Long userId, Integer from, Integer size);

    @Query("""
            select e from Event e where e.id in :ids
            order by e.id
            """)
    List<Event> findByIds(List<Long> ids);

    @Query("""
            select e
            from Event e
            where e.initiator.id = :userId and e.id = :eventId
            order by e.id
            """)
    Event getEventByIdAndUserId(Long userId, Long eventId);

    @Query("""
            select e
            from Event e
            where (:users is null or e.initiator.id in :users)
            and (:states is null or e.state in :states)
            and (:categories is null or e.category.id in :categories)
            and e.eventDate between :rangeStart and :rangeEnd
            order by e.id
            limit :size offset :from
            """)
    List<Event> getAdminEvents(List<Long> users,
                               List<State> states,
                               List<Long> categories,
                               LocalDateTime rangeStart,
                               LocalDateTime rangeEnd,
                               Integer from,
                               Integer size);

    @Query("""
            select e
            from Event e
            where e.id = :eventId and e.state = :state
            """)
    Event getPublishedEventById(Long eventId, State state);
}
