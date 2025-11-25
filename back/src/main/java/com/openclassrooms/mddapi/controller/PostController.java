package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.CommentRequest;
import com.openclassrooms.mddapi.dto.CommentResponse;
import com.openclassrooms.mddapi.dto.MessageResponse;
import com.openclassrooms.mddapi.dto.PostRequest;
import com.openclassrooms.mddapi.dto.PostResponse;
import com.openclassrooms.mddapi.security.CustomUserDetails;
import com.openclassrooms.mddapi.service.CommentService;
import com.openclassrooms.mddapi.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur pour la gestion des articles (posts)
 */
@RestController
@RequestMapping("/api/posts")
public class PostController
{
    private final PostService postService;
    private final CommentService commentService;

    public PostController(PostService postService, CommentService commentService)
    {
        this.postService = postService;
        this.commentService = commentService;
    }

    /**
     * Crée un nouvel article
     */
    @PostMapping
    public ResponseEntity<?> createPost(
            @Valid @RequestBody PostRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        try
        {
            PostResponse post = postService.createPost(request, userDetails.getId());
            return ResponseEntity.ok(post);
        }
        catch (IllegalArgumentException e)
        {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * Récupère tous les articles
     */
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts()
    {
        List<PostResponse> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    /**
     * Récupère un article par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id)
    {
        try
        {
            PostResponse post = postService.getPostById(id);
            return ResponseEntity.ok(post);
        }
        catch (IllegalArgumentException e)
        {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * Récupère les commentaires d'un article
     */
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsByPost(@PathVariable Long id)
    {
        List<CommentResponse> comments = commentService.getCommentsByPost(id);
        return ResponseEntity.ok(comments);
    }

    /**
     * Ajoute un commentaire à un article
     */
    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        try
        {
            // On force l'ID du post dans la requête
            request.setPostId(id);
            CommentResponse comment = commentService.createComment(request, userDetails.getId());
            return ResponseEntity.ok(comment);
        }
        catch (IllegalArgumentException e)
        {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
