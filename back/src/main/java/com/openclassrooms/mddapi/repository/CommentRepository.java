package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository Spring Data JPA pour l'accès aux données de l'entité {@link Comment}.
 * <p>
 * Fournit les opérations CRUD standards ainsi que des méthodes de recherche
 * personnalisées pour filtrer les commentaires par post ou auteur.
 * </p>
 *
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>
{
    /**
     * Récupère tous les commentaires associés à un post spécifique.
     * <p>
     * Les commentaires peuvent être triés par date de création (via la méthode de nommage Spring Data).
     * </p>
     *
     * @param postId l'identifiant du post
     * @return la liste des commentaires du post
     */
    List<Comment> findByPostId(Long postId);
    
    /**
     * Récupère tous les commentaires créés par un utilisateur spécifique.
     *
     * @param authorId l'identifiant de l'auteur
     * @return la liste des commentaires de l'auteur
     */
    List<Comment> findByAuthorId(Long authorId);
}
