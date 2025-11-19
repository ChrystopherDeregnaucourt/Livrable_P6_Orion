package com.openclassrooms.mddapi.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.payload.request.CreateCommentDto;
import com.openclassrooms.mddapi.payload.request.CreatePostDto;
import com.openclassrooms.mddapi.payload.response.CommentDto;
import com.openclassrooms.mddapi.payload.response.PostDetailDto;
import com.openclassrooms.mddapi.payload.response.PostDto;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.services.UserDetailsImpl;
import com.openclassrooms.mddapi.service.ICommentService;
import com.openclassrooms.mddapi.service.IPostService;

@RestController
@RequestMapping("/api/articles")
public class PostController {

    @Autowired
    private IPostService postService;
    
    @Autowired
    private ICommentService commentService;
    
    @Autowired
    private UserRepository userRepository;
    
    // Méthode utilitaire pour convertir Post -> PostDto
    private PostDto convertToDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setDate(post.getCreatedAt());
        dto.setAuthor(post.getAuthor().getUsername());
        dto.setTheme(post.getTopic().getName());
        return dto;
    }

    // GET : Liste de tous les articles
    @GetMapping
    public List<PostDto> getArticles() {
        return postService.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    // GET : Détail d'un article (avec commentaires)
    @GetMapping("/{id}")
    public ResponseEntity<PostDetailDto> getArticle(@PathVariable Long id) {
        Post post = postService.findById(id);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        
        PostDetailDto dto = new PostDetailDto();
        // Mapping manuel des champs
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setDate(post.getCreatedAt());
        dto.setAuthor(post.getAuthor().getUsername());
        dto.setTheme(post.getTopic().getName());
        
        // Récupération et mapping des commentaires
        List<CommentDto> comments = commentService.findByPostId(id).stream()
                .map(c -> new CommentDto(c.getId(), c.getAuthor().getUsername(), c.getContent()))
                .collect(Collectors.toList());
        dto.setComments(comments);
        
        return ResponseEntity.ok(dto);
    }
    
    // POST : Créer un article
    @PostMapping
    public ResponseEntity<?> createArticle(@Valid @RequestBody CreatePostDto createPostDto, Principal principal) {
        UserDetailsImpl userDetails = (UserDetailsImpl) ((Authentication) principal).getPrincipal();
        User user = userRepository.findById(userDetails.getId()).orElse(null);
        
        Post post = postService.create(createPostDto.getTitle(), createPostDto.getContent(), createPostDto.getThemeId(), user);
        if (post == null) {
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok(convertToDto(post));
    }
    
    // POST : Ajouter un commentaire à un article
    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long id, @Valid @RequestBody CreateCommentDto commentDto, Principal principal) {
        UserDetailsImpl userDetails = (UserDetailsImpl) ((Authentication) principal).getPrincipal();
        User user = userRepository.findById(userDetails.getId()).orElse(null);
        Post post = postService.findById(id);
        
        if (post == null || user == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Comment comment = commentService.create(commentDto.getContent(), post, user);
        
        return ResponseEntity.ok(new CommentDto(comment.getId(), comment.getAuthor().getUsername(), comment.getContent()));
    }
}