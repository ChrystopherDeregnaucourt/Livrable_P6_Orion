package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO de requête pour la connexion d'un utilisateur.
 * <p>
 * Permet l'authentification via email ou nom d'utilisateur.
 * Tous les champs sont obligatoires (validés via {@code @NotBlank}).
 * </p>
 *
 */
public class LoginRequest
{
    /**
     * Email ou nom d'utilisateur pour la connexion.
     * L'authentification accepte les deux formats.
     */
    @NotBlank
    private String emailOrUsername;

    /**
     * Mot de passe de l'utilisateur.
     * Sera comparé avec le hash BCrypt stocké en base.
     */
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
