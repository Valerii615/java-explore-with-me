package ru.practicum.explore_with_me.comments.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.comments.models.dto.CommentFullDto;
import ru.practicum.explore_with_me.comments.models.dto.CommentUpdateModeration;
import ru.practicum.explore_with_me.comments.services.CommentService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explore_with_me.util.Util.DATE_FORMAT;

@Slf4j
@RestController
@RequestMapping("/admin/comments")
@AllArgsConstructor
public class CommentAdminController {
    private final CommentService commentService;

    @GetMapping
    public List<CommentFullDto> getComments(@RequestParam(required = false) List<Long> users,
                                            @RequestParam(required = false) List<Long> comments,
                                            @DateTimeFormat(pattern = DATE_FORMAT)
                                            @RequestParam(required = false) LocalDateTime rangeStart,
                                            @DateTimeFormat(pattern = DATE_FORMAT)
                                            @RequestParam(required = false) LocalDateTime rangeEnd,
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size) {
        log.info("Start of endpoint processing (get) /admin/comments?users={}&comments={}&rangeStart={}&rangeEnd={}&from={}&size={}", users, comments, rangeStart, rangeEnd, from, size);
        return commentService.getComments(users, comments, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{commentId}")
    public CommentFullDto updateCommentStatusModeration(@PathVariable Long commentId,
                                                        @RequestBody @Valid CommentUpdateModeration commentUpdateModeration) {
        log.info("Start endpoint processing (patch) /admin/comments/{}", commentId);
        return commentService.updateCommentStatusModeration(commentId, commentUpdateModeration);
    }
}
