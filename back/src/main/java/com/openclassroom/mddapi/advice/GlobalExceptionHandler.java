package com.openclassroom.mddapi.advice;

import com.openclassroom.mddapi.exceptions.BadRequestException;
import com.openclassroom.mddapi.exceptions.ConflictException;
import com.openclassroom.mddapi.exceptions.ForbiddenException;
import com.openclassroom.mddapi.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

/**
 * Global controller advice to handle exceptions across the application.
 * Centralizes error handling to provide consistent and structured responses
 * for all client applications.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex) {
        log.warn("Conflict exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("Data integrity violation: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("Un conflit de données est survenu sur le serveur."));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "La ressource demandée est introuvable.";
        log.info("Resource not found: {}", message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(message));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "Accès refusé.";
        log.warn("Forbidden access attempt: {}", message);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(message));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        log.warn("Authentication failed: Bad credentials");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Identifiants incorrects"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        log.error("An unexpected error occurred: ", ex); // Log avec stacktrace complète
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Une erreur technique est survenue."));
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

        log.warn("Bad request: {}", ex.getMessage());

        if (ex instanceof BadRequestException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Requête invalide."));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.warn("Malformed JSON request: {}", ex.getMostSpecificCause().getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Le format du JSON est invalide."));
    }

    public record ErrorResponse(String message) {
    }
}