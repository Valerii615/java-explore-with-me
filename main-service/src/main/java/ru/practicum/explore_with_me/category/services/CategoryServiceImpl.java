package ru.practicum.explore_with_me.category.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.category.mappers.CategoryMapper;
import ru.practicum.explore_with_me.category.models.Category;
import ru.practicum.explore_with_me.category.models.dto.CategoryDto;
import ru.practicum.explore_with_me.category.repositories.CategoryRepository;
import ru.practicum.explore_with_me.exceptions.NotFoundException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto addCategory(CategoryDto categoryDto) {
        log.info("Add category: {}", categoryDto);
        Category category = categoryRepository.save(categoryMapper.toCategory(categoryDto));
        log.info("Saved category: {}", category);
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        log.info("Update category: {}", categoryDto);
        Category category = getCategoryById(catId);
        category.setName(categoryDto.getName());
        Category updatedCategory = categoryRepository.save(category);
        log.info("Updated category: {}", updatedCategory);
        return categoryMapper.toCategoryDto(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        log.info("Delete category: {}", catId);
        getCategoryById(catId);
        categoryRepository.deleteById(catId);
        log.info("Deleted category: {}", catId);
    }

    @Override
    public List<CategoryDto> getAllCategories(Integer from, Integer size) {
        log.info("Get all categories from {} size {}", from, size);
        List<CategoryDto> categoryDtoList = categoryRepository.getAllCategories(from, size);
        log.info("Found {} categories", categoryDtoList.size());
        return categoryDtoList;
    }

    @Override
    public CategoryDto getCategoryDtoById(Long catId) {
        log.info("Get category by id {}", catId);
        Category category = getCategoryById(catId);
        log.info("Found category: {}", category);
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    public Category getCategoryById(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id " + catId + " not found"));
    }


}
