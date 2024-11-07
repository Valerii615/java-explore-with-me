package ru.practicum.explore_with_me.user;

import ru.practicum.explore_with_me.user.models.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);

    List<UserDto> getAllUsers(List<Long> ids, Integer from, Integer size);

    void deleteUser(Long userId);
}