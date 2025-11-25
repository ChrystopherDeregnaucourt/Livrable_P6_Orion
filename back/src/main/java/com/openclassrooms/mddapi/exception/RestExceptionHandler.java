package com.openclassrooms.mddapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;

/**
 * Cette classe convertit certaines exceptions en réponses HTTP claires
 */
@ControllerAdvice
public class RestExceptionHandler
{
    // Gestion de l'erreur d'inscription lorsque l'email est déjà utilisé
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException exception)
    {
        // On renvoie un code 400 avec le message d'erreur pour informer le client
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    // Gestion des erreurs déclenchées manuellement avec ResponseStatusException
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatus(ResponseStatusException exception)
    {
        // On renvoie la réponse HTTP configurée dans l'exception d'origine
        return ResponseEntity.status(exception.getStatusCode()).body(exception.getReason());
    }

    // Gestion de l'erreur de taille de fichier trop importante
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException exception)
    {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
            .body("La taille du fichier dépasse la limite autorisée (10MB maximum)");
    }

    // Gestion des erreurs de validation des champs (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationErrors(MethodArgumentNotValidException exception)
    {
        StringBuilder errorMessage = new StringBuilder("Erreur de validation: ");
        
        exception.getBindingResult().getFieldErrors().forEach(error -> 
            errorMessage.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ")
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage.toString());
    }
}
