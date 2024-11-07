package ru.practicum.explore_with_me.user.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.exceptions.NotFoundException;
import ru.practicum.explore_with_me.user.mappers.UserMapper;
import ru.practicum.explore_with_me.user.models.User;
import ru.practicum.explore_with_me.user.models.dto.UserDto;
import ru.practicum.explore_with_me.user.repositories.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto addUser(UserDto userDto) {
        log.info("Adding user: {}", userDto);
        User savedUser = userRepository.save(userMapper.toUser(userDto));
        log.info("Saved user: {}", savedUser);
        return userMapper.toUserDto(savedUser);
    }

    @Override
    public List<UserDto> getAllUsers(List<Long> ids, Integer from, Integer size) {
        log.info("Getting all users: {}, from: {}, size: {}", ids, from, size);
        List<UserDto> userDtoList = userRepository.getAllUsers(ids, from, size);
        log.info("Found {} users", userDtoList.size());
        return userDtoList;
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        log.info("Deleting user: {}", userId);
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        userRepository.deleteById(userId);
        log.info("Deleted user: {}", userId);
    }


}
