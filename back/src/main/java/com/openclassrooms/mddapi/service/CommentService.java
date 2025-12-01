package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.CommentRequest;
import com.openclassrooms.mddapi.dto.CommentResponse;
import com.openclassrooms.mddapi.entity.Comment;
import com.openclassrooms.mddapi.entity.Post;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.PostRepository;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service de gestion des commentaires.
 * <p>
 * Gère la création, la recherche et la conversion des commentaires.
 * Coordonne avec UserService et PostRepository pour obtenir les données
 * complètes des commentaires.
 * </p>
 *
 */
@Service
public class CommentService
{
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    /**
     * Constructeur avec injection des dépendances.
     *
     * @param commentRepository le repository pour accéder aux données des commentaires
     * @param postRepository    le repository pour accéder aux données des posts
     * @param userService       le service pour gérer les utilisateurs
     */
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserService userService)
    {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userService = userService;
    }

    /**
     * Crée un nouveau commentaire sur un post.
     *
     * @param request  les données du commentaire (contenu, postId)
     * @param authorId l'identifiant de l'auteur du commentaire
     * @return le DTO du commentaire créé
     * @throws IllegalArgumentException si l'utilisateur ou le post n'existe pas
     */
    @Transactional
    public CommentResponse createComment(CommentRequest request, Long authorId)
    {
        // Récupère l'auteur et le post
        User author = userService.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Article introuvable"));

        // Crée le commentaire
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setPost(post);
        comment.setAuthor(author);

        Comment savedComment = commentRepository.save(comment);
        return toResponse(savedComment);
    }

    /**
     * Récupère tous les commentaires d'un post spécifique.
     *
     * @param postId l'identifiant du post
     * @return la liste des commentaires du post
     */
    public List<CommentResponse> getCommentsByPost(Long postId)
    {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une entité Comment en CommentResponse.
     *
     * @param comment l'entité commentaire à convertir
     * @return le DTO de réponse
     */
    public CommentResponse toResponse(Comment comment)
    {
        CommentResponse response = new CommentResponse();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
                .withZone(ZoneId.systemDefault());

        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setPostId(comment.getPost().getId());
        response.setAuthorId(comment.getAuthor().getId());
        response.setAuthorName(comment.getAuthor().getUsername());
        response.setCreatedAt(formatter.format(comment.getCreatedAt()));

        return response;
    }
}
