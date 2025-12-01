package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de requête pour la création d'un nouveau topic.
 * <p>
 * Le titre est obligatoire (3-100 caractères).
 * La description est optionnelle (max 2000 caractères).
 * </p>
 *
 */
public class TopicRequest
{
    /**
     * Titre du topic (obligatoire, 3-100 caractères).
     */
    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 3, max = 100, message = "Le titre doit contenir entre 3 et 100 caractères")
    private String title;
    
    /**
     * Description du topic (optionnelle, max 2000 caractères).
     */
    @Size(max = 2000, message = "La description ne doit pas dépasser 2000 caractères")
    private String description;

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
}
