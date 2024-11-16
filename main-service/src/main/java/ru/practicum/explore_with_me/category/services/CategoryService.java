package ru.practicum.explore_with_me.category.services;

import ru.practicum.explore_with_me.category.models.Category;
import ru.practicum.explore_with_me.category.models.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto addCategory(CategoryDto category);

    CategoryDto updateCategory(Long catId, CategoryDto categoryDto);

    void deleteCategory(Long catId);

    List<CategoryDto> getAllCategories(Integer from, Integer size);

    CategoryDto getCategoryDtoById(Long catId);

    Category getCategoryById(Long catId);
}
