package com.openclassrooms.mddapi.entity;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Entité JPA représentant un topic (thème) de l'application.
 * <p>
 * Un topic est un sujet/thème auquel les utilisateurs peuvent s'abonner
 * pour recevoir des posts (articles) associés. Les utilisateurs peuvent
 * créer des posts liés à un topic spécifique.
 * </p>
 * <p>
 * Les horodatages sont gérés automatiquement via les callbacks JPA
 * {@link #onCreate()} et {@link #onUpdate()}.
 * </p>
 *
 */
@Entity
@Table(name = "topics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Topic
{
    /**
     * Identifiant unique du topic (clé primaire auto-générée).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id")
    private Long id;

    /**
     * Titre du topic (obligatoire).
     * Représente le nom du thème visible par les utilisateurs.
     */
    @Column(nullable = false)
    private String title;

    /**
     * Description détaillée du topic (optionnelle, max 2000 caractères).
     * Explique le contenu et l'objectif du thème.
     */
    @Column(length = 2000)
    private String description;

    /**
     * Date et heure de création du topic.
     * Initialisée automatiquement via {@link #onCreate()}.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * Date et heure de la dernière modification du topic.
     * Mise à jour automatiquement via {@link #onUpdate()}.
     */
    @Column(name = "updated_at")
    private Instant updatedAt;

    /**
     * Callback JPA appelé automatiquement avant la première sauvegarde en base.
     * Initialise les horodatages createdAt et updatedAt.
     */
    @PrePersist
    protected void onCreate()
    {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    /**
     * Callback JPA appelé automatiquement avant chaque mise à jour en base.
     * Met à jour le champ updatedAt avec la date/heure actuelle.
     */
    @PreUpdate
    protected void onUpdate()
    {
        updatedAt = Instant.now();
    }
}
