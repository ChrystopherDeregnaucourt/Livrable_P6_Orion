package com.openclassrooms.mddapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO de réponse contenant les informations d'un commentaire.
 * <p>
 * Inclut les détails de l'auteur et du post associé.
 * </p>
 *
 */
public class CommentResponse
{
    /**
     * Identifiant unique du commentaire.
     */
    private Long id;
    
    /**
     * Identifiant du post sur lequel le commentaire a été posté.
     */
    private Long postId;
    
    /**
     * Identifiant de l'auteur du commentaire.
     */
    private Long authorId;
    
    /**
     * Nom d'utilisateur de l'auteur du commentaire.
     */
    private String authorName;
    
    /**
     * Contenu textuel du commentaire.
     */
    private String content;
    
    /**
     * Date de création du commentaire au format "yyyy/MM/dd".
     */
    @JsonProperty("created_at")
    private String createdAt;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getPostId()
    {
        return postId;
    }

    public void setPostId(Long postId)
    {
        this.postId = postId;
    }

    public Long getAuthorId()
    {
        return authorId;
    }

    public void setAuthorId(Long authorId)
    {
        this.authorId = authorId;
    }

    public String getAuthorName()
    {
        return authorName;
    }

    public void setAuthorName(String authorName)
    {
        this.authorName = authorName;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(String createdAt)
    {
        this.createdAt = createdAt;
    }
}
