package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.MessageResponse;
import com.openclassrooms.mddapi.dto.UpdateUserRequest;
import com.openclassrooms.mddapi.dto.UserResponse;
import com.openclassrooms.mddapi.entity.Topic;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.CustomUserDetails;
import com.openclassrooms.mddapi.service.TopicService;
import com.openclassrooms.mddapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur pour la gestion des utilisateurs (notamment les abonnements aux topics)
 */
@RestController
@RequestMapping("/api/users")
public class UserController
{
    private final UserService userService;
    private final UserRepository userRepository;
    private final TopicService topicService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, UserRepository userRepository, TopicService topicService, PasswordEncoder passwordEncoder)
    {
        this.userService = userService;
        this.userRepository = userRepository;
        this.topicService = topicService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Met à jour les informations de l'utilisateur connecté
     */
    @PutMapping("/me")
    @Transactional
    public ResponseEntity<?> updateCurrentUser(
            @Valid @RequestBody UpdateUserRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        try
        {
            User user = userService.findById(userDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

            // Mise à jour des champs si fournis
            if (request.getUsername() != null && !request.getUsername().trim().isEmpty())
            {
                // Vérifier si le username est déjà utilisé par un autre utilisateur
                if (userRepository.findByUsername(request.getUsername()).isPresent() &&
                    !user.getUsername().equals(request.getUsername()))
                {
                    return ResponseEntity.badRequest()
                            .body(new MessageResponse("Ce nom d'utilisateur est déjà utilisé"));
                }
                user.setUsername(request.getUsername());
            }

            if (request.getEmail() != null && !request.getEmail().trim().isEmpty())
            {
                // Vérifier si l'email est déjà utilisé par un autre utilisateur
                if (userRepository.findByEmail(request.getEmail()).isPresent() &&
                    !user.getEmail().equals(request.getEmail()))
                {
                    return ResponseEntity.badRequest()
                            .body(new MessageResponse("Cet email est déjà utilisé"));
                }
                user.setEmail(request.getEmail());
            }

            if (request.getPassword() != null && !request.getPassword().trim().isEmpty())
            {
                // Hasher le nouveau mot de passe
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }

            userRepository.save(user);

            // Préparer la réponse
            UserResponse response = new UserResponse();
            response.setId(user.getId());
            response.setEmail(user.getEmail());
            response.setUsername(user.getUsername());
            response.setCreatedAt(user.getCreatedAt() != null ? user.getCreatedAt().toString() : null);
            response.setUpdatedAt(user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : null);

            return ResponseEntity.ok(response);
        }
        catch (IllegalArgumentException e)
        {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * S'abonne à un topic
     */
    @PostMapping("/me/subscriptions/{topicId}")
    @Transactional
    public ResponseEntity<?> subscribeToTopic(
            @PathVariable Long topicId,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        try
        {
            // Récupère l'utilisateur et le topic
            User user = userService.findById(userDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
            Topic topic = topicService.findById(topicId);

            // Vérifie si déjà abonné
            if (user.getSubscriptions().contains(topic))
            {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Vous êtes déjà abonné à ce thème"));
            }

            // Ajoute l'abonnement
            user.getSubscriptions().add(topic);
            userRepository.save(user);

            return ResponseEntity.ok(new MessageResponse("Abonnement réussi"));
        }
        catch (IllegalArgumentException e)
        {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * Se désabonne d'un topic
     */
    @DeleteMapping("/me/subscriptions/{topicId}")
    @Transactional
    public ResponseEntity<?> unsubscribeFromTopic(
            @PathVariable Long topicId,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        try
        {
            // Récupère l'utilisateur et le topic
            User user = userService.findById(userDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
            Topic topic = topicService.findById(topicId);

            // Vérifie si l'utilisateur est bien abonné
            if (!user.getSubscriptions().contains(topic))
            {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Vous n'êtes pas abonné à ce thème"));
            }

            // Retire l'abonnement
            user.getSubscriptions().remove(topic);
            userRepository.save(user);

            return ResponseEntity.ok(new MessageResponse("Désabonnement réussi"));
        }
        catch (IllegalArgumentException e)
        {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
