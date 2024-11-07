package ru.practicum.explore_with_me.event;

import org.springframework.data.domain.Pageable;
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


    @Query("""
            select e
            from Event e
            where 
                (:text is null or 
                    e.annotation like concat('%', :text, '%') 
                    or e.description like concat('%', :text, '%'))
            and (:categories is null or e.category.id in :categories)
            and (:paid is null or e.paid = :paid)
            and e.eventDate between :rangeStart and :rangeEnd
            and (:onlyAvailable is false or e.participantLimit > e.confirmedRequests)
            group by e.id
            order by 
                case 
                    when :sort is null then e.eventDate
                    else :sort 
                end
            """)
    List<Event> getAllPublishedEvents(String text,
                                      List<Long> categories,
                                      Boolean paid,
                                      LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd,
                                      Boolean onlyAvailable,
                                      String sort,
                                      Pageable pageable);
}
