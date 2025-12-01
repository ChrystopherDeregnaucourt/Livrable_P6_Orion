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
 * Entité JPA représentant un commentaire sur un post.
 * <p>
 * Un commentaire est créé par un utilisateur ({@link User}) et associé
 * à un post spécifique ({@link Post}). Il permet aux utilisateurs de
 * réagir et d'échanger sur les articles.
 * </p>
 * <p>
 * Les relations ManyToOne vers User et Post sont chargées en mode LAZY
 * pour optimiser les performances.
 * </p>
 *
 */
@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment
{
    /**
     * Identifiant unique du commentaire (clé primaire auto-générée).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Contenu textuel du commentaire (obligatoire).
     * Stocké en format TEXT pour permettre des commentaires longs.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * Date et heure de création du commentaire.
     * Initialisée automatiquement à la création de l'instance.
     */
    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    /**
     * Utilisateur auteur de ce commentaire (obligatoire).
     * Relation ManyToOne avec chargement lazy.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    /**
     * Post sur lequel ce commentaire a été posté (obligatoire).
     * Relation ManyToOne avec chargement lazy.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
