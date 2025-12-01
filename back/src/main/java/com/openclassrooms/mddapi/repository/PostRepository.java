package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository Spring Data JPA pour l'accès aux données de l'entité {@link Post}.
 * <p>
 * Fournit les opérations CRUD standards ainsi que des méthodes de recherche
 * personnalisées pour filtrer les posts par topic ou auteur.
 * </p>
 *
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long>
{
    /**
     * Récupère tous les posts associés à un topic spécifique.
     *
     * @param topicId l'identifiant du topic
     * @return la liste des posts du topic
     */
    List<Post> findByTopicId(Long topicId);
    
    /**
     * Récupère tous les posts créés par un utilisateur spécifique.
     *
     * @param authorId l'identifiant de l'auteur
     * @return la liste des posts de l'auteur
     */
    List<Post> findByAuthorId(Long authorId);
}
