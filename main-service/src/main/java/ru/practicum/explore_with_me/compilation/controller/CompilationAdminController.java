package ru.practicum.explore_with_me.compilation.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.compilation.model.dto.CompilationDto;
import ru.practicum.explore_with_me.compilation.model.dto.NewCompilationDto;
import ru.practicum.explore_with_me.compilation.model.dto.UpdateCompilationRequest;
import ru.practicum.explore_with_me.compilation.services.CompilationService;

@Slf4j
@RestController
@RequestMapping("/admin/compilations")
@AllArgsConstructor
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Start of endpoint processing (post) /admin/compilations");
        return compilationService.addCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Start of endpoint processing (delete) /admin/compilations/{}", compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        log.info("Start of endpoint processing (patch) /admin/compilations/{}", compId);
        return compilationService.updateCompilation(compId, updateCompilationRequest);
    }
}
