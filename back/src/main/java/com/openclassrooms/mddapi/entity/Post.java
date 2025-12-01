package com.openclassrooms.mddapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.Instant;

/**
 * Entité JPA représentant un post (article) de l'application.
 * <p>
 * Un post est un article créé par un utilisateur ({@link User}) et associé
 * à un topic ({@link Topic}). Les posts peuvent recevoir des commentaires
 * via l'entité {@link Comment}.
 * </p>
 * <p>
 * Les relations ManyToOne vers User et Topic sont chargées en mode LAZY
 * pour optimiser les performances.
 * </p>
 *
 */
@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post
{
    /**
     * Identifiant unique du post (clé primaire auto-générée).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    /**
     * Titre du post (obligatoire).
     * Résume le contenu de l'article.
     */
    @Column(nullable = false)
    private String title;

    /**
     * Contenu textuel du post (obligatoire).
     * Stocké en format TEXT pour permettre de longs articles.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * Date et heure de création du post.
     * Initialisée automatiquement à la création de l'instance.
     */
    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    /**
     * Topic (thème) auquel ce post est associé (obligatoire).
     * Relation ManyToOne avec chargement lazy.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    /**
     * Utilisateur auteur de ce post (obligatoire).
     * Relation ManyToOne avec chargement lazy.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;
}
