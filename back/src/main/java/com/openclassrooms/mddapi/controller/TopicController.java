package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.TopicRequest;
import com.openclassrooms.mddapi.dto.TopicResponse;
import com.openclassrooms.mddapi.security.CustomUserDetails;
import com.openclassrooms.mddapi.service.TopicService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST pour la gestion des topics (thèmes).
 * <p>
 * Expose les endpoints pour récupérer la liste des topics
 * et créer de nouveaux topics.
 * </p>
 * <p>
 * Endpoints :
 * </p>
 * <ul>
 *   <li>GET /api/topics - Récupération de tous les topics</li>
 *   <li>POST /api/topics - Création d'un nouveau topic</li>
 * </ul>
 *
 */
@RestController
@RequestMapping("/api/topics")
public class TopicController
{
    private final TopicService topicService;

    /**
     * Constructeur avec injection du service.
     *
     * @param topicService le service de gestion des topics
     */
    public TopicController(TopicService topicService)
    {
        this.topicService = topicService;
    }

    /**
     * Récupère la liste de tous les topics avec l'indicateur d'abonnement.
     * <p>
     * Si l'utilisateur est connecté, le champ 'subscribed' indique
     * s'il est abonné à chaque topic.
     * </p>
     *
     * @param userDetails les détails de l'utilisateur connecté (peut être null)
     * @return 200 OK avec la liste des topics
     */
    @GetMapping
    public ResponseEntity<List<TopicResponse>> getAllTopics(
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        Long userId = userDetails != null ? userDetails.getId() : null;
        List<TopicResponse> topics = topicService.getAllTopics(userId);
        return ResponseEntity.ok(topics);
    }

    /**
     * Crée un nouveau topic.
     *
     * @param request les données du topic (titre, description)
     * @return 201 Created avec le topic créé
     */
    @PostMapping
    public ResponseEntity<TopicResponse> createTopic(@Valid @RequestBody TopicRequest request)
    {
        TopicResponse createdTopic = topicService.createTopic(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTopic);
    }
}
