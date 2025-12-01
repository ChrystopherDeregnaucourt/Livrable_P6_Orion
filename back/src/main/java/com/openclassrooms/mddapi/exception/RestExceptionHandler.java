package com.openclassrooms.mddapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;

/**
 * Gestionnaire global des exceptions pour l'API REST.
 * <p>
 * Intercepte les exceptions non gérées et les convertit en réponses HTTP
 * appropriées avec des messages clairs pour le client.
 * </p>
 * <p>
 * Annoté avec {@code @ControllerAdvice} pour s'appliquer à tous les controllers.
 * </p>
 *
 */
@ControllerAdvice
public class RestExceptionHandler
{
    /**
     * Gestion des erreurs de validation métier (IllegalArgumentException).
     * <p>
     * Ex: email déjà utilisé, username déjà pris, topic introuvable, etc.
     * </p>
     *
     * @param exception l'exception levée
     * @return 400 Bad Request avec le message d'erreur
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException exception)
    {
        // On renvoie un code 400 avec le message d'erreur pour informer le client
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    /**
     * Gestion des exceptions HTTP personnalisées (ResponseStatusException).
     *
     * @param exception l'exception avec le statut et le message configurés
     * @return la réponse HTTP avec le statut et le message de l'exception
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatus(ResponseStatusException exception)
    {
        return ResponseEntity.status(exception.getStatusCode()).body(exception.getReason());
    }

    /**
     * Gestion de l'erreur de fichier trop volumineux lors de l'upload.
     *
     * @param exception l'exception de dépassement de taille
     * @return 413 Payload Too Large avec un message explicite
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException exception)
    {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
            .body("La taille du fichier dépasse la limite autorisée (10MB maximum)");
    }

    /**
     * Gestion des erreurs de validation des champs de requête (annotations @Valid).
     * <p>
     * Concatène tous les messages d'erreur de validation en une seule réponse.
     * </p>
     *
     * @param exception l'exception contenant les erreurs de validation
     * @return 400 Bad Request avec la liste des erreurs de validation
     */
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
