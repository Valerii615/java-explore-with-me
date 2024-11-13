package ru.practicum.explore_with_me.comments.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.comments.models.dto.CommentDto;
import ru.practicum.explore_with_me.comments.services.CommentService;

@Slf4j
@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentPublicController {
    private final CommentService commentService;

    @GetMapping("/{commentId}")
    public CommentDto getCommentById(@PathVariable Long commentId) {
        log.info("Start of endpoint processing (get) /comments/{}", commentId);
        return commentService.getCommentById(commentId);
    }

    @GetMapping
    public CommentDto getCommentsByEventId(@RequestParam Long eventId) {
        log.info("Start of endpoint processing (get) /comments?eventId={}", eventId);
        return commentService.getCommentsByEventId(eventId);
    }
}
