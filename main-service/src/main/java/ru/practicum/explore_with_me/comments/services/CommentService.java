package ru.practicum.explore_with_me.comments.services;

import ru.practicum.explore_with_me.comments.models.dto.CommentDto;
import ru.practicum.explore_with_me.comments.models.dto.CommentFullDto;
import ru.practicum.explore_with_me.comments.models.dto.CommentRequest;
import ru.practicum.explore_with_me.comments.models.dto.CommentUpdateModeration;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {
    CommentDto getCommentById(Long commentId);

    CommentDto getCommentsByEventId(Long eventId);

    CommentFullDto addComment(Long userId, Long eventId, CommentRequest commentRequest);

    List<CommentDto> getCommentsByUserId(Long userId, Integer from, Integer size);

    CommentFullDto getCommentByIdAndUserId(Long userId, Long commentId);

    CommentFullDto updateComment(Long userId, Long commentId, CommentRequest commentRequest);

    List<CommentFullDto> getComments(List<Long> users,
                                     List<Long> comments,
                                     LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd,
                                     Integer from,
                                     Integer size);

    CommentFullDto updateCommentStatusModeration(Long commentId, CommentUpdateModeration commentUpdateModeration);
}
