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
 * Service de gestion des topics (thèmes).
 * <p>
 * Gère la création, la recherche et la conversion des topics.
 * Permet également de déterminer si un utilisateur est abonné à un topic.
 * </p>
 *
 */
@Service
public class TopicService
{
    private final TopicRepository topicRepository;
    private final UserService userService;

    /**
     * Constructeur avec injection des dépendances.
     *
     * @param topicRepository le repository pour accéder aux données des topics
     * @param userService     le service utilisateur pour vérifier les abonnements
     */
    public TopicService(TopicRepository topicRepository, UserService userService)
    {
        this.topicRepository = topicRepository;
        this.userService = userService;
    }

    /**
     * Récupère tous les topics avec l'indicateur d'abonnement pour un utilisateur.
     * <p>
     * Si userId est fourni, le champ 'subscribed' indique si l'utilisateur est abonné.
     * Si userId est null, le champ 'subscribed' reste null.
     * </p>
     *
     * @param userId l'identifiant de l'utilisateur connecté (peut être null)
     * @return la liste de tous les topics avec leur statut d'abonnement
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
     * Recherche un topic par son identifiant.
     *
     * @param id l'identifiant du topic
     * @return l'entité Topic
     * @throws IllegalArgumentException si le topic n'existe pas
     */
    public Topic findById(Long id)
    {
        return topicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Topic introuvable avec l'ID : " + id));
    }

    /**
     * Crée un nouveau topic.
     *
     * @param request les données du topic à créer (titre, description)
     * @return le DTO du topic créé
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
     * Convertit une entité Topic en TopicResponse (sans indicateur d'abonnement).
     *
     * @param topic l'entité topic à convertir
     * @return le DTO de réponse
     */
    public TopicResponse toResponse(Topic topic)
    {
        return toResponse(topic, null);
    }
    
    /**
     * Convertit une entité Topic en TopicResponse avec le statut d'abonnement.
     * <p>
     * Si un utilisateur est fourni, détermine s'il est abonné au topic.
     * </p>
     *
     * @param topic l'entité topic à convertir
     * @param user  l'utilisateur pour lequel vérifier l'abonnement (peut être null)
     * @return le DTO de réponse avec l'indicateur d'abonnement
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
