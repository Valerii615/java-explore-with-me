package ru.practicum.explore_with_me.category.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.category.services.CategoryService;
import ru.practicum.explore_with_me.category.models.dto.CategoryDto;

@Slf4j
@RestController
@RequestMapping("/admin/categories")
@AllArgsConstructor
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@RequestBody @Valid CategoryDto categoryDto) {
        log.info("Start of endpoint processing (post) /admin/categories");
        return categoryService.addCategory(categoryDto);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@PathVariable Long catId, @RequestBody @Valid CategoryDto categoryDto) {
        log.info("Start of endpoint processing (patch) /admin/categories/{}", catId);
        return categoryService.updateCategory(catId, categoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.info("Start of endpoint processing (delete) /admin/categories/{}", catId);
        categoryService.deleteCategory(catId);
    }


}
