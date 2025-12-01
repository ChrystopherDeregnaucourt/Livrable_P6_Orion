package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository Spring Data JPA pour l'accès aux données de l'entité {@link Topic}.
 * <p>
 * Fournit les opérations CRUD standards ainsi que des méthodes de recherche
 * personnalisées.
 * </p>
 *
 */
@Repository
public interface TopicRepository extends JpaRepository<Topic, Long>
{
    /**
     * Recherche un topic par son titre (insensible à la casse).
     *
     * @param title le titre du topic recherché
     * @return le topic trouvé, ou null si aucun topic ne correspond
     */
    Topic findByTitleIgnoreCase(String title);
}
