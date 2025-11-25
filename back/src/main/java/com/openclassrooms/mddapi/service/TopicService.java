package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.TopicRequest;
import com.openclassrooms.mddapi.dto.TopicResponse;
import com.openclassrooms.mddapi.entity.Topic;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.repository.TopicRepository;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service pour la gestion des topics (thèmes)
 */
@Service
public class TopicService
{
    private final TopicRepository topicRepository;
    private final UserService userService;

    public TopicService(TopicRepository topicRepository, UserService userService)
    {
        this.topicRepository = topicRepository;
        this.userService = userService;
    }

    /**
     * Récupère tous les topics avec le statut d'abonnement pour un utilisateur
     */
    public List<TopicResponse> getAllTopics(Long userId)
    {
        List<Topic> topics = topicRepository.findAll();
        User user = null;
        
        if (userId != null)
        {
            user = userService.findById(userId).orElse(null);
        }
        
        final User currentUser = user;
        return topics.stream()
                .map(topic -> toResponse(topic, currentUser))
                .collect(Collectors.toList());
    }

    /**
     * Récupère un topic par son ID
     */
    public Topic findById(Long id)
    {
        return topicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Topic introuvable avec l'ID : " + id));
    }

    /**
     * Crée un nouveau topic
     */
    @Transactional
    public TopicResponse createTopic(TopicRequest request)
    {
        // Création de l'entité Topic
        Topic topic = new Topic();
        topic.setTitle(request.getTitle());
        topic.setDescription(request.getDescription());
        
        // Sauvegarde en base
        Topic savedTopic = topicRepository.save(topic);
        
        // Conversion en DTO de réponse
        return toResponse(savedTopic);
    }

    /**
     * Convertit une entité Topic en TopicResponse
     */
    public TopicResponse toResponse(Topic topic)
    {
        return toResponse(topic, null);
    }
    
    /**
     * Convertit une entité Topic en TopicResponse avec le statut d'abonnement
     */
    public TopicResponse toResponse(Topic topic, User user)
    {
        TopicResponse response = new TopicResponse();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
                .withZone(ZoneId.systemDefault());

        response.setId(topic.getId());
        response.setTitle(topic.getTitle());
        response.setDescription(topic.getDescription());
        response.setCreatedAt(formatter.format(topic.getCreatedAt()));
        response.setUpdatedAt(formatter.format(topic.getUpdatedAt()));
        
        // Définir le statut d'abonnement si un utilisateur est fourni
        if (user != null)
        {
            boolean isSubscribed = user.getSubscriptions() != null && 
                                   user.getSubscriptions().contains(topic);
            response.setSubscribed(isSubscribed);
        }

        return response;
    }
}
