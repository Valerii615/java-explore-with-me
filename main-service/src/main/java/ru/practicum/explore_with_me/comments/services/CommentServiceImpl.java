package ru.practicum.explore_with_me.comments.services;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.comments.mappers.CommentMapper;
import ru.practicum.explore_with_me.comments.models.Comment;
import ru.practicum.explore_with_me.comments.models.Moderation;
import ru.practicum.explore_with_me.comments.models.dto.CommentDto;
import ru.practicum.explore_with_me.comments.models.dto.CommentFullDto;
import ru.practicum.explore_with_me.comments.models.dto.CommentRequest;
import ru.practicum.explore_with_me.comments.models.dto.CommentUpdateModeration;
import ru.practicum.explore_with_me.comments.repositories.CommentRepository;
import ru.practicum.explore_with_me.event.models.Event;
import ru.practicum.explore_with_me.event.models.State;
import ru.practicum.explore_with_me.event.repositories.EventRepository;
import ru.practicum.explore_with_me.exceptions.BadRequestException;
import ru.practicum.explore_with_me.exceptions.ConflictException;
import ru.practicum.explore_with_me.exceptions.NotFoundException;
import ru.practicum.explore_with_me.user.models.User;
import ru.practicum.explore_with_me.user.repositories.UserRepository;
import ru.practicum.explore_with_me.util.Util;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explore_with_me.comments.models.QComment.comment;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public CommentDto getCommentById(Long commentId) {
        log.info("Get comment by id: {}", commentId);
        Comment comment = commentRepository.findByIdAndModeration(commentId, Moderation.PUBLISHED);
        if (comment == null) {
            throw new NotFoundException("Comment with id " + commentId + " not found");
        }
        log.info("Found comment {}", comment);
        return commentMapper.toDto(comment);
    }

    @Override
    public List<CommentDto> getCommentsByEventId(Long eventId) {
        log.info("Get comment by event id: {}", eventId);
        List<Comment> commentList = commentRepository.findByEventIdAndModeration(eventId, Moderation.PUBLISHED);
        log.info("Found {} comments", commentList.size());
        return commentMapper.toDtoList(commentList);
    }

    @Override
    @Transactional
    public CommentFullDto addComment(Long userId, Long eventId, CommentRequest commentRequest) {
        log.info("Adding comment userId: {}, eventId: {}, comment: {}", userId, eventId, commentRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User with id: {} not found", userId);
                    return new NotFoundException("User with id " + userId + " not found");
                });
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    log.error("Event with id: {} not found", eventId);
                    return new NotFoundException("Event with id " + eventId + " not found");
                });
        if (event.getState() != State.PUBLISHED) {
            throw new ConflictException("Event with id " + eventId + " is not published");
        }
        Comment savedComment = commentRepository
                .save(Comment.builder()
                        .event(event)
                        .commentator(user)
                        .text(commentRequest.getText())
                        .moderation(Moderation.PENDING)
                        .createdAt(LocalDateTime.now())
                        .build());
        log.info("Comment saved: {}", savedComment);
        return commentMapper.toFullDto(savedComment);
    }

    @Override
    public List<CommentDto> getCommentsByUserId(Long userId, Integer from, Integer size) {
        log.info("Getting comments by userId: {}, from: {}, size: {}", userId, from, size);
        Pageable pageable = PageRequest.of(from / size, size);
        List<Comment> commentList = commentRepository.findByCommentatorId(userId, pageable);
        log.info("Comments found: {}", commentList);
        return commentMapper.toDtoList(commentList);
    }

    @Override
    public CommentFullDto getCommentByIdAndUserId(Long userId, Long commentId) {
        log.info("Getting comment userId: {}, commentId: {}", userId, commentId);
        Comment comment = commentRepository.findByIdAndCommentatorId(commentId, userId);
        if (comment == null) {
            throw new NotFoundException("Comment with id " + commentId + " not found");
        }
        log.info("Found comment: {}", comment);
        return commentMapper.toFullDto(comment);
    }

    @Override
    @Transactional
    public CommentFullDto updateComment(Long userId, Long commentId, CommentRequest commentRequest) {
        log.info("Updating comment userId: {}, commentId: {}, comment: {}", userId, commentId, commentRequest);
        Comment comment = commentRepository.findByIdAndCommentatorId(commentId, userId);
        if (comment == null) {
            throw new NotFoundException("Comment with id " + commentId + " not found");
        }
        comment.setText(commentRequest.getText());
        Comment updatedComment = commentRepository.save(comment);
        log.info("Comment updated: {}", updatedComment);
        return commentMapper.toFullDto(updatedComment);
    }

    @Override
    public List<CommentFullDto> getComments(List<Long> users,
                                            List<Long> comments,
                                            LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd,
                                            Integer from, Integer size) {
        log.info("Getting comments by admin parameters");
        if (rangeStart == null) {
            rangeStart = Util.DATE_TIME_MIN;
        }
        if (rangeEnd == null) {
            rangeEnd = Util.DATE_TIME_MAX;
        }
        if (rangeEnd.isBefore(rangeStart)) {
            throw new BadRequestException("The start time must be earlier than the end.");
        }
        Pageable pageable = PageRequest.of(from / size, size);
        BooleanBuilder builder = new BooleanBuilder();
        if (users != null) {
            builder.and(comment.commentator.id.in(users));
        }
        if (comments != null) {
            builder.and(comment.commentator.id.in(comments));
        }
        builder.and(comment.createdAt.between(rangeStart, rangeEnd));
        List<Comment> commentList = commentRepository.findAll(builder, pageable).getContent();
        log.info("Comments with parameters found: {}", commentList);
        return commentMapper.toFullDtoList(commentList);
    }

    @Override
    @Transactional
    public CommentFullDto updateCommentStatusModeration(Long commentId, CommentUpdateModeration commentUpdateModeration) {
        log.info("Updating comment moderation status, commentId: {}, status: {}", commentId, commentUpdateModeration);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id " + commentId + " not found"));
        comment.setModeration(commentUpdateModeration.getModeration());
        Comment updatedComment = commentRepository.save(comment);
        log.info("Comment moderation status updated: {}", updatedComment);
        return commentMapper.toFullDto(updatedComment);
    }
}
