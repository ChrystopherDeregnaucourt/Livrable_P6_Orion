package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO de requête pour la mise à jour du profil utilisateur.
 * <p>
 * Tous les champs sont optionnels - seuls les champs fournis seront mis à jour.
 * Les validations sont appliquées uniquement si les champs sont présents.
 * </p>
 *
 */
public class UpdateUserRequest
{
    /**
     * Nouveau nom d'utilisateur (optionnel, 3-50 caractères).
     * Doit être unique dans le système.
     */
    @Size(min = 3, max = 50, message = "Le nom d'utilisateur doit contenir entre 3 et 50 caractères")
    private String username;

    /**
     * Nouvelle adresse email (optionnelle).
     * Doit être un email valide et unique dans le système.
     */
    @Email(message = "L'email doit être valide")
    private String email;

    /**
     * Nouveau mot de passe (optionnel, 8-40 caractères).
     * Mêmes contraintes de complexité que pour l'inscription.
     * Sera hashé avec BCrypt avant stockage.
     */
    @Size(min = 8, max = 40, message = "Le mot de passe doit contenir au moins 8 caractères")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*()_\\-{}\\[\\]:;\"'<>,.?/~`|]).{8,}$",
        message = "Le mot de passe doit contenir au moins un chiffre, une minuscule, une majuscule et un caractère spécial"
    )
    private String password;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
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
