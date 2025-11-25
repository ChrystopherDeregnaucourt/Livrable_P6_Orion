package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour l'accès aux données des posts
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long>
{
    // Méthode pour trouver tous les posts d'un topic spécifique
    List<Post> findByTopicId(Long topicId);
    
    // Méthode pour trouver tous les posts d'un auteur spécifique
    List<Post> findByAuthorId(Long authorId);
}