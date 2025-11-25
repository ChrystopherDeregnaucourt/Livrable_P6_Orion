package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour l'accès aux données des topics
 */
@Repository
public interface TopicRepository extends JpaRepository<Topic, Long>
{
    // Méthode pour rechercher un topic par son titre (insensible à la casse)
    Topic findByTitleIgnoreCase(String title);
}