package ru.practicum.explore_with_me.category.mappers;

import org.mapstruct.Mapper;
import ru.practicum.explore_with_me.category.models.Category;
import ru.practicum.explore_with_me.category.models.dto.CategoryDto;

@Mapper
public interface CategoryMapper {

    Category toCategory(CategoryDto categoryDto);

    CategoryDto toCategoryDto(Category category);
}
