package ru.practicum.explore_with_me.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({BadRequestException.class,
            DataIntegrityViolationException.class,
            MethodArgumentTypeMismatchException.class,
            NotFoundException.class,
            ConflictException.class})
    public ResponseEntity<ApiError> handleException(Exception exception) {
        log.error(exception.getMessage());
        HttpStatus status;
        String reason;
        String message;
        switch (exception) {
            case DataIntegrityViolationException e -> {
                status = HttpStatus.CONFLICT;
                reason = "Integrity constraint has been violated";
                message = e.getMessage();
            }
            case MethodArgumentTypeMismatchException e -> {
                status = HttpStatus.BAD_REQUEST;
                reason = "Incorrectly made request.";
                message = e.getMessage();
            }
            case NotFoundException e -> {
                status = HttpStatus.NOT_FOUND;
                reason = "The required object was not found.";
                message = e.getMessage();
            }
            case BadRequestException e -> {
                status = HttpStatus.BAD_REQUEST;
                reason = "Incorrectly made request.";
                message = e.getMessage();
            }
            case ConflictException e -> {
                status = HttpStatus.CONFLICT;
                reason = "Integrity constraint has been violated.";
                message = e.getMessage();
            }
            default -> throw new IllegalStateException("Unexpected value: " + exception);
        }
        ApiError apiError = ApiError.builder()
                .status(status.name())
                .reason(reason)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(apiError);

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleException(final MethodArgumentNotValidException e) {
        String errors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("Field: %s. Error: %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining(", "));

        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .reason("Incorrectly made request.")
                .message(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
