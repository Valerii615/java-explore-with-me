package ru.practicum.explore_with_me.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.user.models.dto.UserDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/users")
@AllArgsConstructor
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody @Valid UserDto userDto) {
        log.info("Start of endpoint processing (post) /admin/users");
        return userServiceImpl.addUser(userDto);
    }

    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam(required = false) List<Long> ids,
                                     @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                     @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Start of endpoint processing (get) /admin/users?ids={}&from={}&size={}", ids, from, size);
        return userServiceImpl.getAllUsers(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        log.info("Start of endpoint processing (delete) /admin/users/{}", userId);
        userServiceImpl.deleteUser(userId);
    }
}
