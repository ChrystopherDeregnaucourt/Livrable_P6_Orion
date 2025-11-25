package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO pour la demande de création d'un post
 */
public class PostRequest
{
    @NotBlank
    private String title;

    @NotBlank
    private String content;

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
