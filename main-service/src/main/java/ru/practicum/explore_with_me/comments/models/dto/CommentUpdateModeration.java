package ru.practicum.explore_with_me.comments.models.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore_with_me.comments.models.Moderation;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentUpdateModeration {
    @NotNull
    private Moderation moderation;
}
