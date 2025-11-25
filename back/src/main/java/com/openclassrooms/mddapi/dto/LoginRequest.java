package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO pour la demande de connexion
 */
public class LoginRequest
{
    @NotBlank
    private String emailOrUsername;

    @NotBlank
    private String password;

    public String getEmailOrUsername()
    {
        return emailOrUsername;
    }

    public void setEmailOrUsername(String emailOrUsername)
    {
        this.emailOrUsername = emailOrUsername;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
