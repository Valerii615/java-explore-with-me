package ru.practicum.explore_with_me.compilation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore_with_me.compilation.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query("""
            select c from Compilation c
            where (:pinned is null or c.pinned = :pinned)
            order by c.id
            limit :size offset :from
            """)
    List<Compilation> findCompilationsByParameters(Boolean pinned, Integer from, Integer size);
}
