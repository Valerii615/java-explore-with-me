package ru.practicum.explore_with_me.comments.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore_with_me.comments.models.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findByIdAndCommentatorId(Long commentId,Long commentatorId);
    List<Comment> findByCommentatorId(Long commentatorId, Pageable pageable);
}
