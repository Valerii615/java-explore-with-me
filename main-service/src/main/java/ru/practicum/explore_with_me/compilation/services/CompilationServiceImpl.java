package ru.practicum.explore_with_me.compilation.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.compilation.mappers.CompilationMapper;
import ru.practicum.explore_with_me.compilation.model.Compilation;
import ru.practicum.explore_with_me.compilation.model.dto.CompilationDto;
import ru.practicum.explore_with_me.compilation.model.dto.NewCompilationDto;
import ru.practicum.explore_with_me.compilation.model.dto.UpdateCompilationRequest;
import ru.practicum.explore_with_me.compilation.repositories.CompilationRepository;
import ru.practicum.explore_with_me.event.repositories.EventRepository;
import ru.practicum.explore_with_me.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        log.info("Add compilation: {}", newCompilationDto);
        Compilation compilation = compilationMapper.toCompilation(newCompilationDto);
        if (newCompilationDto.getEvents() != null) {
            compilation.setEvents(eventRepository.findByIds(newCompilationDto.getEvents()));
        } else {
            compilation.setEvents(new ArrayList<>());
        }
        Compilation saved = compilationRepository.save(compilation);
        log.info("Saved: {}", saved);
        return compilationMapper.toCompilationDto(saved);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        log.info("Delete compilation: {}", compId);
        compilationRepository.deleteById(compId);
        log.info("Deleted: {}", compId);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        log.info("Update compilationId: {}, compilation: {}", compId, updateCompilationRequest);
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id " + compId + " not found"));
        if (updateCompilationRequest.getEvents() != null) {
            compilation.setEvents(eventRepository.findByIds(updateCompilationRequest.getEvents()));
        } else {
            compilation.setEvents(new ArrayList<>());
        }
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        compilationRepository.save(compilation);
        log.info("Updated: {}", compilation);
        return compilationMapper.toCompilationDto(compilation);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        log.info("Get compilations by parameters, pinned: {}, from: {}, size: {}", pinned, from, size);
        List<Compilation> compilationList = compilationRepository.findCompilationsByParameters(pinned, from, size);
        log.info("Found {} compilations", compilationList.size());
        return compilationMapper.toCompilationDtoList(compilationList);
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        log.info("Get compilation by id: {}", compId);
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id " + compId + " not found"));
        log.info("Found: {}", compilation);
        return compilationMapper.toCompilationDto(compilation);
    }
}
