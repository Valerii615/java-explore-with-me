package ru.practicum.explore_with_me.compilation.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.compilation.services.CompilationService;
import ru.practicum.explore_with_me.compilation.model.dto.CompilationDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/compilations")
@AllArgsConstructor
public class CompilationPublicController {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Start of endpoint processing (get) /compilations?pinned={}}&from={}&size={}", pinned, from, size);
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        log.info("Start of endpoint processing (get) /compilations/{}", compId);
        return compilationService.getCompilationById(compId);
    }
}
