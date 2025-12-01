package com.openclassrooms.mddapi.dto;

/**
 * DTO de réponse pour les opérations d'authentification (login et register).
 * <p>
 * Contient le jeton JWT généré après une connexion ou inscription réussie.
 * Le client doit stocker ce token et l'inclure dans l'en-tête Authorization
 * des requêtes suivantes (format: "Bearer {token}").
 * </p>
 *
 */
public class AuthResponse
{
    /**
     * Jeton JWT utilisé pour authentifier les requêtes futures.
     */
    private String token;

    /**
     * Constructeur par défaut.
     * Nécessaire pour la désérialisation JSON.
     */
    public AuthResponse()
    {
    }

    /**
     * Constructeur avec initialisation du token.
     *
     * @param token le jeton JWT à retourner au client
     */
    public AuthResponse(String token)
    {
        this.token = token;
    }

    /**
     * Récupère le jeton JWT.
     *
     * @return le jeton JWT
     */
    public String getToken()
    {
        return token;
    }

    /**
     * Définit le jeton JWT.
     *
     * @param token le nouveau jeton JWT
     */
    public void setToken(String token)
    {
        this.token = token;
    }
}
