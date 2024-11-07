package ru.practicum.explore_with_me.compilation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explore_with_me.compilation.model.Compilation;
import ru.practicum.explore_with_me.compilation.model.dto.CompilationDto;
import ru.practicum.explore_with_me.compilation.model.dto.NewCompilationDto;

import java.util.List;

@Mapper
public interface CompilationMapper {
//    @Mapping(source = "events", target = "events", qualifiedByName = "eventsToIds")
//    NewCompilationDto toNewDto(Compilation compilation);

    @Mapping(target = "events", ignore = true)
    Compilation toCompilation(NewCompilationDto newCompilationDto);

    CompilationDto toCompilationDto(Compilation compilation);

    List<CompilationDto> toCompilationDtoList(List<Compilation> compilations);
//
//    @Named("eventsToIds")
//    default List<Long> eventsToIds(List<Event> events) {
//        if (events == null) {
//            return null;
//        }
//        return events.stream()
//                .map(Event::getId)
//                .collect(Collectors.toList());
//    }
}
