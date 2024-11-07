package ru.practicum.explore_with_me.requests.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore_with_me.requests.model.ParticipationRequest;
import ru.practicum.explore_with_me.requests.model.Status;

import java.util.List;

public interface RequestsRepository extends JpaRepository<ParticipationRequest, Long> {

    @Query("""
            select count(p) from ParticipationRequest p where p.id = :id and p.status = :status
            """)
    Integer countByIdAndStatus(Long id, Status status);

    Integer countByStatusAndEventId(Status status, Long eventId);

    List<ParticipationRequest> findByIdAndStatus(Long id, Status status);

    List<ParticipationRequest> findByStatusAndEventId(Status status, Long eventId);

    List<ParticipationRequest> findByRequesterId(Long id);

    ParticipationRequest findByIdAndRequesterId(Long id, Long requesterId);

    @Query("""
            select r from ParticipationRequest r
            where r.event.id=:eventId and r.event.initiator.id=:userId
            group by r.id
            """)
    List<ParticipationRequest> findRequestsByEvent(Long userId, Long eventId);

    @Query("""
            select r from ParticipationRequest r
            where r.event.id=:eventId
            and r.event.initiator.id=:userId
            and r.id in :ids
            group by r.id
            """)
    List<ParticipationRequest> findRequestsByEventAndIds(Long userId, Long eventId, List<Long> ids);


}
