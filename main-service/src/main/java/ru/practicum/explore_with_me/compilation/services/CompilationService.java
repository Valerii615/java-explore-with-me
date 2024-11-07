package ru.practicum.explore_with_me.compilation.services;

import ru.practicum.explore_with_me.compilation.model.dto.CompilationDto;
import ru.practicum.explore_with_me.compilation.model.dto.NewCompilationDto;
import ru.practicum.explore_with_me.compilation.model.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest);

    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationById(Long compId);
}
