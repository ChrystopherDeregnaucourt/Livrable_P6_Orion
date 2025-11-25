package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour l'accès aux données des commentaires
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>
{
    // Méthode pour récupérer les commentaires d'un post, souvent triés par date
    List<Comment> findByPostId(Long postId);
    
    // Méthode pour récupérer les commentaires d'un auteur spécifique
    List<Comment> findByAuthorId(Long authorId);
}