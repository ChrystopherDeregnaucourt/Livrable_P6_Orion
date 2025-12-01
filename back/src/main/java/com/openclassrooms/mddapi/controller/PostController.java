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
 * Contrôleur REST pour la gestion des posts (articles) et de leurs commentaires.
 * <p>
 * Expose les endpoints pour créer des posts, récupérer des posts,
 * et gérer les commentaires associés.
 * </p>
 * <p>
 * Endpoints :
 * </p>
 * <ul>
 *   <li>POST /api/posts - Création d'un post</li>
 *   <li>GET /api/posts - Récupération de tous les posts</li>
 *   <li>GET /api/posts/{id} - Récupération d'un post spécifique avec commentaires</li>
 *   <li>GET /api/posts/{id}/comments - Récupération des commentaires d'un post</li>
 *   <li>POST /api/posts/{id}/comments - Ajout d'un commentaire sur un post</li>
 * </ul>
 *
 */
@RestController
@RequestMapping("/api/posts")
public class PostController
{
    private final PostService postService;
    private final CommentService commentService;

    /**
     * Constructeur avec injection des services.
     *
     * @param postService    le service de gestion des posts
     * @param commentService le service de gestion des commentaires
     */
    public PostController(PostService postService, CommentService commentService)
    {
        this.postService = postService;
        this.commentService = commentService;
    }

    /**
     * Crée un nouvel article.
     * <p>
     * L'utilisateur connecté sera automatiquement défini comme auteur.
     * </p>
     *
     * @param request     les données du post (titre, contenu, topicId)
     * @param userDetails les détails de l'utilisateur connecté
     * @return 200 OK avec le post créé, 400 Bad Request si erreur
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
     * Récupère tous les articles (sans les commentaires).
     *
     * @return 200 OK avec la liste de tous les posts
     */
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts()
    {
        List<PostResponse> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    /**
     * Récupère un article spécifique par son identifiant (avec les commentaires).
     *
     * @param id l'identifiant du post
     * @return 200 OK avec le post et ses commentaires, 400 Bad Request si introuvable
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
     * Récupère les commentaires d'un article.
     *
     * @param id l'identifiant du post
     * @return 200 OK avec la liste des commentaires
     */
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsByPost(@PathVariable Long id)
    {
        List<CommentResponse> comments = commentService.getCommentsByPost(id);
        return ResponseEntity.ok(comments);
    }

    /**
     * Ajoute un commentaire sur un article.
     * <p>
     * L'utilisateur connecté sera automatiquement défini comme auteur du commentaire.
     * Le postId est extrait du path parameter.
     * </p>
     *
     * @param id          l'identifiant du post sur lequel commenter
     * @param request     les données du commentaire (contenu)
     * @param userDetails les détails de l'utilisateur connecté
     * @return 200 OK avec le commentaire créé, 400 Bad Request si erreur
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
