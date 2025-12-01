package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO de requête pour la création d'un nouveau commentaire.
 * <p>
 * Le commentaire sera automatiquement associé à l'utilisateur connecté comme auteur.
 * Le postId peut être fourni dans le corps de la requête ou via le path parameter.
 * </p>
 *
 */
public class CommentRequest
{
    /**
     * Identifiant du post sur lequel poster le commentaire.
     * Peut être défini automatiquement par le controller.
     */
    private Long postId;

    /**
     * Contenu textuel du commentaire (obligatoire).
     */
    @NotBlank
    private String content;

    public Long getPostId()
    {
        return postId;
    }

    public void setPostId(Long postId)
    {
        this.postId = postId;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }
}
