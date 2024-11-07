package ru.practicum.explore_with_me.user;

import org.mapstruct.Mapper;
import ru.practicum.explore_with_me.user.models.User;
import ru.practicum.explore_with_me.user.models.dto.UserDto;
import ru.practicum.explore_with_me.user.models.dto.UserShortDto;

@Mapper
public interface UserMapper {

    User toUser(UserDto userDto);

    UserDto toUserDto(User user);

    UserShortDto toUserShortDto(User user);
}
