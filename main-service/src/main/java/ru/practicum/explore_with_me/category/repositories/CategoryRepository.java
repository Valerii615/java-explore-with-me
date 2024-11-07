package ru.practicum.explore_with_me.category.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore_with_me.category.models.Category;
import ru.practicum.explore_with_me.category.models.dto.CategoryDto;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("""
            select new ru.practicum.explore_with_me.category.models.dto.CategoryDto(c.id, c.name)
            from Category c
            order by c.id asc
            limit :size offset :from
            """)
    List<CategoryDto> getAllCategories(Integer from, Integer size);
}
