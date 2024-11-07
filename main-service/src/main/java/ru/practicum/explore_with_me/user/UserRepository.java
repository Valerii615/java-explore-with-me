package ru.practicum.explore_with_me.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore_with_me.user.models.User;
import ru.practicum.explore_with_me.user.models.dto.UserDto;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
            select new ru.practicum.explore_with_me.user.models.dto.UserDto(u.id, u.name, u.email)
            from User u
            where (:ids is null or u.id in :ids)
            order by u.id
            limit :size offset :from
            """)
    List<UserDto> getAllUsers(List<Long> ids, Integer from, Integer size);
}
