package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de requête pour la création d'un nouveau post (article).
 * <p>
 * Tous les champs sont obligatoires. Le post sera automatiquement associé
 * à l'utilisateur connecté comme auteur.
 * </p>
 *
 */
public class PostRequest
{
    /**
     * Titre du post (obligatoire).
     */
    @NotBlank
    private String title;

    /**
     * Contenu textuel du post (obligatoire).
     */
    @NotBlank
    private String content;

    /**
     * Identifiant du topic auquel le post est associé (obligatoire).
     */
    @NotNull
    private Long topicId;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public Long getTopicId()
    {
        return topicId;
    }

    public void setTopicId(Long topicId)
    {
        this.topicId = topicId;
    }
}
