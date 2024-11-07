package ru.practicum.explore_with_me;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.models.EndpointHit;
import ru.practicum.explore_with_me.models.ViewStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@AllArgsConstructor
public class StatsController {
    private final StatsService statsServiceImpl;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addEndpoint(@Valid @RequestBody EndpointHit endpointHit) {
        log.info("Start of endpoint processing (post) /hit");
        statsServiceImpl.addEndpoint(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam String start,
                                    @RequestParam String end,
                                    @RequestParam(required = false) List<String> uris,
                                    @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        log.info("Start of endpoint processing (get) /stats");
        LocalDateTime startTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endTime = LocalDateTime.parse(end, formatter);
        return statsServiceImpl.getStats(startTime, endTime, uris, unique);
    }
}
