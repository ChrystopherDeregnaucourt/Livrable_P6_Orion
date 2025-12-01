package com.openclassrooms.mddapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.Instant;
import java.util.List;

/**
 * Entité JPA représentant un utilisateur de l'application.
 * <p>
 * Cette classe gère les informations de base de l'utilisateur ainsi que
 * ses abonnements aux topics (thèmes). Elle utilise une relation ManyToMany
 * pour gérer les abonnements via la table de jointure "subscriptions".
 * </p>
 * <p>
 * Les contraintes d'unicité sont appliquées sur l'email et le username
 * pour éviter les doublons.
 * </p>
 *
 */
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "username")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User
{
    /**
     * Identifiant unique de l'utilisateur (clé primaire auto-générée).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Adresse email de l'utilisateur (unique et obligatoire).
     * Utilisée comme identifiant de connexion.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Nom d'utilisateur (unique et obligatoire).
     * Affiché publiquement dans l'application.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Mot de passe hashé avec BCrypt (obligatoire).
     * Ne doit jamais être stocké en clair.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Date et heure de création du compte utilisateur.
     * Initialisée automatiquement à la création.
     */
    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    /**
     * Date et heure de la dernière mise à jour du profil utilisateur.
     * Mise à jour automatiquement via la méthode {@link #onUpdate()}.
     */
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    /**
     * Liste des topics (thèmes) auxquels l'utilisateur est abonné.
     * Relation ManyToMany avec chargement lazy pour optimiser les performances.
     * Un utilisateur peut suivre plusieurs topics, et un topic peut avoir plusieurs abonnés.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "subscriptions",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "topic_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Topic> subscriptions;

    /**
     * Constructeur pour créer un nouvel utilisateur avec les informations essentielles.
     * Les horodatages (createdAt, updatedAt) sont initialisés automatiquement.
     *
     * @param email    l'adresse email de l'utilisateur (doit être unique)
     * @param username le nom d'utilisateur (doit être unique)
     * @param password le mot de passe hashé avec BCrypt
     */
    public User(String email, String username, String password)
    {
        this.email = email;
        this.username = username;
        this.password = password;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * Méthode de callback JPA appelée automatiquement avant chaque mise à jour en base.
     * Met à jour le champ {@code updatedAt} avec la date/heure actuelle.
     */
    @PreUpdate
    protected void onUpdate()
    {
        this.updatedAt = Instant.now();
    }
}
