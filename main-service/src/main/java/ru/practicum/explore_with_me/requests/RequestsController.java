package ru.practicum.explore_with_me.requests;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.requests.model.dto.ParticipationRequestDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/requests")
@AllArgsConstructor
public class RequestsController {
    private final RequestsService requestsService;

    @GetMapping
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId) {
        log.info("Start of endpoint processing (get) /users/{}/requests", userId);
        return requestsService.getRequests(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ParticipationRequestDto addRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("Start of endpoint processing (post) /users/{}/requests?eventId={}", userId, eventId);
        return requestsService.addRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("Start of endpoint processing (patch) /users/{}/requests/{}/cancel", userId, requestId);
        return requestsService.cancelRequest(userId, requestId);
    }
}
