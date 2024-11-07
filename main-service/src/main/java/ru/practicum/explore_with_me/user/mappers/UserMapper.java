package ru.practicum.explore_with_me.user.mappers;

import org.mapstruct.Mapper;
import ru.practicum.explore_with_me.user.models.User;
import ru.practicum.explore_with_me.user.models.dto.UserDto;

@Mapper
public interface UserMapper {

    User toUser(UserDto userDto);

    UserDto toUserDto(User user);
}
