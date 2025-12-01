package com.openclassrooms.mddapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * DTO de réponse contenant les informations d'un post (article).
 * <p>
 * Contient les détails du post ainsi que les informations du topic et de l'auteur associés.
 * La liste des commentaires est incluse lors de la récupération d'un post spécifique.
 * </p>
 *
 */
public class PostResponse
{
    /**
     * Identifiant unique du post.
     */
    private Long id;
    
    /**
     * Titre du post.
     */
    private String title;
    
    /**
     * Contenu textuel du post.
     */
    private String content;
    
    /**
     * Identifiant du topic associé.
     */
    private Long topicId;
    
    /**
     * Titre du topic associé.
     */
    private String topicTitle;
    
    /**
     * Identifiant de l'auteur du post.
     */
    private Long authorId;
    
    /**
     * Nom d'utilisateur de l'auteur du post.
     */
    private String authorName;
    
    /**
     * Date de création du post au format "yyyy/MM/dd".
     */
    @JsonProperty("created_at")
    private String createdAt;
    
    /**
     * Liste des commentaires associés au post.
     * Null dans les listes, remplie lors de la récupération d'un post spécifique.
     */
    private List<CommentResponse> comments;

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

    public String getTopicTitle()
    {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle)
    {
        this.topicTitle = topicTitle;
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

    public String getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(String createdAt)
    {
        this.createdAt = createdAt;
    }

    public List<CommentResponse> getComments()
    {
        return comments;
    }

    public void setComments(List<CommentResponse> comments)
    {
        this.comments = comments;
    }
}
