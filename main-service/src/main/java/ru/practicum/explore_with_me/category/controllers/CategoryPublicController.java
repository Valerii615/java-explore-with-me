package ru.practicum.explore_with_me.category.controllers;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.category.services.CategoryService;
import ru.practicum.explore_with_me.category.models.dto.CategoryDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryPublicController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getAllCategories(@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Start of endpoint processing (get) /categories?from={}&size={}", from, size);
        return categoryService.getAllCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        log.info("Start of endpoint processing (get) /categories?catId={}", catId);
        return categoryService.getCategoryById(catId);
    }
}
