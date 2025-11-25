package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO pour la demande de création d'un commentaire
 */
public class CommentRequest
{
    private Long postId;

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
