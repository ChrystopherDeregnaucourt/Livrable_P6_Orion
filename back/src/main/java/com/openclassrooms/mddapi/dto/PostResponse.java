package com.openclassrooms.mddapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * DTO pour la réponse d'un post
 */
public class PostResponse
{
    private Long id;
    private String title;
    private String content;
    
    // ID du topic
    private Long topicId;
    
    // Titre du topic
    private String topicTitle;
    
    // ID de l'auteur du post
    private Long authorId;
    
    // Nom de l'auteur du post
    private String authorName;
    
    // Date de création formatée
    @JsonProperty("created_at")
    private String createdAt;
    
    // Liste des commentaires
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
