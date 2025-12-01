package com.openclassrooms.mddapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * DTO de réponse contenant les informations d'un utilisateur.
 * <p>
 * Utilisé pour exposer les données utilisateur au client sans révéler
 * d'informations sensibles (comme le mot de passe hashé).
 * Inclut la liste des topics auxquels l'utilisateur est abonné.
 * </p>
 *
 */
public class UserResponse
{
    /**
     * Identifiant unique de l'utilisateur.
     */
    private Long id;
    
    /**
     * Adresse email de l'utilisateur.
     */
    private String email;
    
    /**
     * Nom d'utilisateur affiché publiquement.
     */
    private String username;

    /**
     * Date de création du compte au format "yyyy/MM/dd".
     */
    @JsonProperty("created_at")
    private String createdAt;

    /**
     * Date de dernière modification du profil au format "yyyy/MM/dd".
     */
    @JsonProperty("updated_at")
    private String updatedAt;

    /**
     * Liste des topics auxquels l'utilisateur est abonné.
     */
    private List<TopicResponse> subscriptions;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(String createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt()
    {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public List<TopicResponse> getSubscriptions()
    {
        return subscriptions;
    }

    public void setSubscriptions(List<TopicResponse> subscriptions)
    {
        this.subscriptions = subscriptions;
    }
}
