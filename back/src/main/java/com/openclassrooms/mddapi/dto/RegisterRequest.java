package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO de requête pour l'inscription d'un nouvel utilisateur.
 * <p>
 * Contient les validations strictes pour assurer la sécurité :
 * </p>
 * <ul>
 *   <li>Username : 3-20 caractères</li>
 *   <li>Email : format valide</li>
 *   <li>Password : minimum 8 caractères avec complexité (chiffre, majuscule, minuscule, caractère spécial)</li>
 * </ul>
 *
 */
public class RegisterRequest
{
    /**
     * Nom d'utilisateur (unique dans le système).
     * Doit contenir entre 3 et 20 caractères.
     */
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    /**
     * Adresse email (unique dans le système).
     * Doit être un email valide et ne pas dépasser 50 caractères.
     */
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    /**
     * Mot de passe de l'utilisateur.
     * Contraintes de sécurité :
     * <ul>
     *   <li>Longueur : 8-40 caractères</li>
     *   <li>Au moins un chiffre</li>
     *   <li>Au moins une lettre minuscule</li>
     *   <li>Au moins une lettre majuscule</li>
     *   <li>Au moins un caractère spécial</li>
     * </ul>
     * Le mot de passe sera hashé avec BCrypt avant stockage.
     */
    @NotBlank
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
