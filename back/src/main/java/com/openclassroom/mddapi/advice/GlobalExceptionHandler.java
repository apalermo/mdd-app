package com.openclassroom.mddapi.advice;

import com.openclassroom.mddapi.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

/**
 * Global controller advice to handle exceptions across the application.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex) {
        log.warn("Data conflict detected: {}", ex.getMessage());
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), "Un conflit de données est survenu.");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("Database integrity violation: {}", ex.getMessage());
        return buildResponse(HttpStatus.CONFLICT, null, "Un conflit de données est survenu sur le serveur.");
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        log.info("Resource not found: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), "La ressource demandée est introuvable.");
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex) {
        log.warn("Access forbidden: {}", ex.getMessage());
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage(), "Accès refusé.");
    }

    @ExceptionHandler(MddBadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(MddBadCredentialsException ex) {
        log.warn("Authentication failure: {}", ex.getMessage());
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), "Identifiants incorrects.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        log.error("Unexpected internal server error: ", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, "Une erreur technique est survenue.");
    }

    @ExceptionHandler({
            BadRequestException.class,
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestExceptions(Exception ex) {
        if (ex instanceof MethodArgumentNotValidException validEx) {
            String validationError = validEx.getBindingResult().getFieldErrors().stream()
                    .map(error -> error.getField() + " : " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            log.warn("Validation failed: {}", validationError);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(validationError));
        }

        log.warn("Bad request exception: {}", ex.getMessage());
        String message = (ex instanceof BadRequestException) ? ex.getMessage() : "Requête invalide.";
        return buildResponse(HttpStatus.BAD_REQUEST, message, "Requête invalide.");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.warn("Malformed JSON payload: {}", ex.getMostSpecificCause().getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, null, "Le format du JSON est invalide.");
    }

    /**
     * Helper method to build a consistent error response.
     */
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, String defaultMessage) {
        String finalMessage = (message != null && !message.isEmpty()) ? message : defaultMessage;
        return ResponseEntity.status(status).body(new ErrorResponse(finalMessage));
    }

    public record ErrorResponse(String message) {
    }
}