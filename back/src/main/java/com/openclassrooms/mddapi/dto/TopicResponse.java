package com.openclassrooms.mddapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO de réponse contenant les informations d'un topic (thème).
 * <p>
 * Inclut un indicateur {@code subscribed} permettant de savoir si
 * l'utilisateur connecté est abonné à ce topic (optionnel selon le contexte).
 * </p>
 *
 */
public class TopicResponse
{
    /**
     * Identifiant unique du topic.
     */
    private Long id;
    
    /**
     * Titre du topic.
     */
    private String title;
    
    /**
     * Description du topic.
     */
    private String description;
    
    /**
     * Indique si l'utilisateur connecté est abonné à ce topic.
     * Null si aucun utilisateur n'est authentifié.
     */
    private Boolean subscribed;
    
    /**
     * Date de création du topic au format "yyyy/MM/dd".
     */
    @JsonProperty("created_at")
    private String createdAt;
    
    /**
     * Date de dernière modification du topic au format "yyyy/MM/dd".
     */
    @JsonProperty("updated_at")
    private String updatedAt;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Boolean getSubscribed()
    {
        return subscribed;
    }

    public void setSubscribed(Boolean subscribed)
    {
        this.subscribed = subscribed;
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
}
