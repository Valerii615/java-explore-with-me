package ru.practicum.explore_with_me.comments.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.comments.models.dto.CommentDto;
import ru.practicum.explore_with_me.comments.models.dto.CommentFullDto;
import ru.practicum.explore_with_me.comments.models.dto.CommentRequest;
import ru.practicum.explore_with_me.comments.services.CommentService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}")
@AllArgsConstructor
public class CommentPrivetController {
    private final CommentService commentService;

    @PostMapping("/comments/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentFullDto addComment(@PathVariable Long userId,
                                     @PathVariable Long eventId,
                                     @RequestBody @Valid CommentRequest commentRequest) {
        log.info("Start of endpoint processing (post) /users/{}/comments/{}", userId, eventId);
        return commentService.addComment(userId, eventId, commentRequest);
    }

    @GetMapping("/comments")
    public List<CommentDto> getCommentsByUserId(@PathVariable Long userId,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Start of endpoint processing (get) /users/{}/comments?from={}&size={}", userId, from, size);
        return commentService.getCommentsByUserId(userId, from, size);
    }

    @GetMapping("/comments/{commentId}")
    public CommentFullDto getCommentByIdAndUserId(@PathVariable Long userId, @PathVariable Long commentId) {
        log.info("Start endpoint processing (get) /users/{}/comments/{}", userId, commentId);
        return commentService.getCommentByIdAndUserId(userId, commentId);
    }

    @PatchMapping("/comments/{commentId}")
    public CommentFullDto updateComment(@PathVariable Long userId,
                                        @PathVariable Long commentId,
                                        @RequestBody @Valid CommentRequest commentRequest) {
        log.info("Start endpoint processing (patch) /users/{}/comments/{}", userId, commentId);
        return commentService.updateComment(userId, commentId, commentRequest);
    }
}
