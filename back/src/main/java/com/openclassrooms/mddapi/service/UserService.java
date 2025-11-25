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
 * Service pour la gestion des utilisateurs
 */
@Service
public class UserService
{
    private final UserRepository userRepository;

    // Outil fourni par Spring pour chiffrer les mots de passe
    private final PasswordEncoder passwordEncoder;

    // Injecte le repository et le PasswordEncoder
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
     * Convertit une entité User en UserResponse
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
     * Convertit une entité Topic en TopicResponse (sans le champ subscribed)
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

    public Optional<User> findByEmail(String email)
    {
        // On délègue au repository pour récupérer l'utilisateur par email
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String username)
    {
        // On délègue au repository pour récupérer l'utilisateur par username
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id)
    {
        // Délègue au repository pour récupérer l'utilisateur par identifiant
        return userRepository.findById(id);
    }
}
