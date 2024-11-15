package ru.practicum.explore_with_me.comments.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explore_with_me.comments.models.Comment;
import ru.practicum.explore_with_me.comments.models.dto.CommentDto;
import ru.practicum.explore_with_me.comments.models.dto.CommentFullDto;

import java.util.List;

@Mapper
public interface CommentMapper {
    CommentDto toDto(Comment comment);

    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "commentator.id", target = "commentatorId")
    CommentFullDto toFullDto(Comment comment);

    List<CommentDto> toDtoList(List<Comment> comments);

    List<CommentFullDto> toFullDtoList(List<Comment> comments);
}
