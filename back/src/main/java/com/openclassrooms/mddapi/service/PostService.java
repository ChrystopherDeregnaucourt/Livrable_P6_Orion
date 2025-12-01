package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.PostRequest;
import com.openclassrooms.mddapi.dto.PostResponse;
import com.openclassrooms.mddapi.entity.Post;
import com.openclassrooms.mddapi.entity.Topic;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.repository.PostRepository;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service de gestion des posts (articles).
 * <p>
 * Gère la création, la recherche et la conversion des posts.
 * Coordonne avec TopicService, UserService et CommentService pour
 * obtenir les données complètes des posts.
 * </p>
 *
 */
@Service
public class PostService
{
    private final PostRepository postRepository;
    private final TopicService topicService;
    private final UserService userService;
    private final CommentService commentService;

    /**
     * Constructeur avec injection des dépendances.
     *
     * @param postRepository  le repository pour accéder aux données des posts
     * @param topicService    le service pour gérer les topics
     * @param userService     le service pour gérer les utilisateurs
     * @param commentService  le service pour gérer les commentaires
     */
    public PostService(PostRepository postRepository, TopicService topicService, UserService userService, CommentService commentService)
    {
        this.postRepository = postRepository;
        this.topicService = topicService;
        this.userService = userService;
        this.commentService = commentService;
    }

    /**
     * Crée un nouvel article.
     *
     * @param request  les données du post à créer (titre, contenu, topicId)
     * @param authorId l'identifiant de l'auteur du post
     * @return le DTO du post créé
     * @throws IllegalArgumentException si l'utilisateur ou le topic n'existe pas
     */
    @Transactional
    public PostResponse createPost(PostRequest request, Long authorId)
    {
        // Récupère l'auteur et le topic
        User author = userService.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
        Topic topic = topicService.findById(request.getTopicId());

        // Crée le post
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setTopic(topic);
        post.setAuthor(author);

        Post savedPost = postRepository.save(post);
        return toResponse(savedPost);
    }

    /**
     * Récupère tous les articles (sans les commentaires).
     *
     * @return la liste de tous les posts
     */
    public List<PostResponse> getAllPosts()
    {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(this::toResponseWithoutComments)
                .collect(Collectors.toList());
    }

    /**
     * Récupère tous les articles d'un topic spécifique (sans les commentaires).
     *
     * @param topicId l'identifiant du topic
     * @return la liste des posts du topic
     */
    public List<PostResponse> getPostsByTopic(Long topicId)
    {
        List<Post> posts = postRepository.findByTopicId(topicId);
        return posts.stream()
                .map(this::toResponseWithoutComments)
                .collect(Collectors.toList());
    }

    /**
     * Récupère un article par son identifiant (avec les commentaires).
     *
     * @param id l'identifiant du post
     * @return le DTO du post avec ses commentaires
     * @throws IllegalArgumentException si le post n'existe pas
     */
    public PostResponse getPostById(Long id)
    {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article introuvable avec l'ID : " + id));
        return toResponse(post);
    }

    /**
     * Convertit une entité Post en PostResponse avec les commentaires.
     *
     * @param post l'entité post à convertir
     * @return le DTO de réponse avec les commentaires
     */
    public PostResponse toResponse(Post post)
    {
        PostResponse response = toResponseWithoutComments(post);
        // Ajoute les commentaires associés au post
        response.setComments(commentService.getCommentsByPost(post.getId()));
        return response;
    }

    /**
     * Convertit une entité Post en PostResponse sans les commentaires.
     * <p>
     * Utilisée pour les listes de posts afin d'optimiser les performances.
     * </p>
     *
     * @param post l'entité post à convertir
     * @return le DTO de réponse sans commentaires
     */
    private PostResponse toResponseWithoutComments(Post post)
    {
        PostResponse response = new PostResponse();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
                .withZone(ZoneId.systemDefault());

        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setTopicId(post.getTopic().getId());
        response.setTopicTitle(post.getTopic().getTitle());
        response.setAuthorId(post.getAuthor().getId());
        response.setAuthorName(post.getAuthor().getUsername());
        response.setCreatedAt(formatter.format(post.getCreatedAt()));

        return response;
    }
}
