package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.RegisterRequest;
import com.openclassrooms.mddapi.dto.TopicResponse;
import com.openclassrooms.mddapi.dto.UserResponse;
import com.openclassrooms.mddapi.entity.Topic;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service de gestion des utilisateurs.
 * <p>
 * Ce service gère la création des comptes utilisateur, la conversion
 * vers les DTOs de réponse, et la recherche d'utilisateurs.
 * Il applique les règles métier comme la vérification d'unicité
 * des emails et usernames, et le chiffrement sécurisé des mots de passe.
 * </p>
 *
 */
@Service
public class UserService
{
    private final UserRepository userRepository;

    /**
     * Outil fourni par Spring Security pour chiffrer les mots de passe avec BCrypt.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructeur avec injection des dépendances.
     *
     * @param userRepository  le repository pour accéder aux données utilisateur
     * @param passwordEncoder l'encodeur pour hacher les mots de passe
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Crée un nouvel utilisateur après vérification de l'unicité.
     * <p>
     * Vérifie que l'email et le username ne sont pas déjà utilisés.
     * Hash le mot de passe avec BCrypt avant stockage.
     * </p>
     *
     * @param request les données d'inscription (email, username, mot de passe)
     * @return l'utilisateur créé et sauvegardé en base
     * @throws IllegalArgumentException si l'email ou le username existe déjà
     */
    @Transactional
    public User createUser(RegisterRequest request)
    {
        // Vérifie si un utilisateur avec le même email existe déjà
        if (userRepository.existsByEmail(request.getEmail()))
        {
            throw new IllegalArgumentException("Cet email est déjà utilisé par un autre compte");
        }

        // Vérifie si un utilisateur avec le même username existe déjà
        if (userRepository.existsByUsername(request.getUsername()))
        {
            throw new IllegalArgumentException("Ce nom d'utilisateur est déjà utilisé");
        }

        // On chiffre le mot de passe avant de le stocker
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getEmail(), request.getUsername(), encodedPassword);
        return userRepository.save(user);
    }

    /**
     * Convertit une entité User en DTO UserResponse.
     * <p>
     * Formate les dates au format "yyyy/MM/dd" et inclut les abonnements.
     * </p>
     *
     * @param user l'entité utilisateur à convertir
     * @return le DTO de réponse avec les informations formatées
     */
    public UserResponse toResponse(User user)
    {
        UserResponse response = new UserResponse();

        // Formatteur pour convertir les dates au format attendu par le frontend
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
                .withZone(ZoneId.systemDefault());

        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setUsername(user.getUsername());

        // Formatage des dates au format attendu par le frontend
        response.setCreatedAt(formatter.format(user.getCreatedAt()));
        response.setUpdatedAt(formatter.format(user.getUpdatedAt()));

        // Convertir les abonnements (topics) en TopicResponse
        if (user.getSubscriptions() != null && !user.getSubscriptions().isEmpty())
        {
            List<TopicResponse> subscriptionsResponse = user.getSubscriptions().stream()
                    .map(this::toTopicResponse)
                    .collect(Collectors.toList());
            response.setSubscriptions(subscriptionsResponse);
        }

        return response;
    }

    /**
     * Convertit une entité Topic en TopicResponse (sans indicateur d'abonnement).
     * <p>
     * Utilisée pour formater les abonnements dans la réponse utilisateur.
     * </p>
     *
     * @param topic l'entité topic à convertir
     * @return le DTO de réponse du topic
     */
    private TopicResponse toTopicResponse(Topic topic)
    {
        TopicResponse response = new TopicResponse();
        response.setId(topic.getId());
        response.setTitle(topic.getTitle());
        response.setDescription(topic.getDescription());
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
                .withZone(ZoneId.systemDefault());
        response.setCreatedAt(formatter.format(topic.getCreatedAt()));
        response.setUpdatedAt(formatter.format(topic.getUpdatedAt()));
        
        return response;
    }

    /**
     * Recherche un utilisateur par son adresse email.
     *
     * @param email l'adresse email recherchée
     * @return un Optional contenant l'utilisateur s'il existe, vide sinon
     */
    public Optional<User> findByEmail(String email)
    {
        return userRepository.findByEmail(email);
    }

    /**
     * Recherche un utilisateur par son nom d'utilisateur.
     *
     * @param username le nom d'utilisateur recherché
     * @return un Optional contenant l'utilisateur s'il existe, vide sinon
     */
    public Optional<User> findByUsername(String username)
    {
        return userRepository.findByUsername(username);
    }

    /**
     * Recherche un utilisateur par son identifiant.
     *
     * @param id l'identifiant de l'utilisateur
     * @return un Optional contenant l'utilisateur s'il existe, vide sinon
     */
    public Optional<User> findById(Long id)
    {
        return userRepository.findById(id);
    }
}
