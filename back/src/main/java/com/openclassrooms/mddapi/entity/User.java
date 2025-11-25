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
 * Entité représentant un utilisateur
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    // Relation ManyToMany pour les abonnements aux topics
    // Un utilisateur peut suivre plusieurs topics
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "subscriptions",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "topic_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Topic> subscriptions;

    // Constructeur pour initialiser les propriétés principales (sans id, createdAt et updatedAt)
    public User(String email, String username, String password)
    {
        this.email = email;
        this.username = username;
        this.password = password;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // Méthode appelée automatiquement avant chaque mise à jour
    @PreUpdate
    protected void onUpdate()
    {
        this.updatedAt = Instant.now();
    }
}
