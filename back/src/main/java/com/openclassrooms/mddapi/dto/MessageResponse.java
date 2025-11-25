package com.openclassrooms.mddapi.dto;

/**
 * DTO pour les réponses de message simple
 */
public class MessageResponse
{
    private String message;

    public MessageResponse()
    {
    }

    public MessageResponse(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
