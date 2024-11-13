package ru.practicum.explore_with_me.comments.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore_with_me.comments.models.Moderation;

import java.time.LocalDateTime;

import static ru.practicum.explore_with_me.util.Util.DATE_FORMAT;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentFullDto {
    private Long id;
    private Long eventId;
    private Long commentatorId;
    private String text;
    private Moderation moderation;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime createdAt;
}
