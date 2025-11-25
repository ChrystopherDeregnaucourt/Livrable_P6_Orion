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
 * Contrôleur pour la gestion des topics (thèmes)
 */
@RestController
@RequestMapping("/api/topics")
public class TopicController
{
    private final TopicService topicService;

    public TopicController(TopicService topicService)
    {
        this.topicService = topicService;
    }

    /**
     * Récupère la liste de tous les topics avec le statut d'abonnement
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
     * Crée un nouveau topic
     */
    @PostMapping
    public ResponseEntity<TopicResponse> createTopic(@Valid @RequestBody TopicRequest request)
    {
        TopicResponse createdTopic = topicService.createTopic(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTopic);
    }
}
