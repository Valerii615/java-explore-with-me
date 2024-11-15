package ru.practicum.explore_with_me.comments.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.explore_with_me.comments.models.Comment;
import ru.practicum.explore_with_me.comments.models.Moderation;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, QuerydslPredicateExecutor<Comment> {
    Comment findByIdAndCommentatorId(Long commentId, Long commentatorId);

    List<Comment> findByCommentatorId(Long commentatorId, Pageable pageable);

    Comment findByIdAndModeration(Long id, Moderation moderation);

    List<Comment> findByEventIdAndModeration(Long eventId, Moderation moderation);
}
