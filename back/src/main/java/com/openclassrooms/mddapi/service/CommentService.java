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
 * Service pour la gestion des commentaires
 */
@Service
public class CommentService
{
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserService userService)
    {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userService = userService;
    }

    /**
     * Crée un nouveau commentaire
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
     * Récupère les commentaires d'un article spécifique
     */
    public List<CommentResponse> getCommentsByPost(Long postId)
    {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une entité Comment en CommentResponse
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
