package com.openclassrooms.mddapi.dto;

/**
 * DTO pour la réponse d'authentification
 * Contient le jeton JWT à renvoyer au client après une connexion ou une inscription
 */
public class AuthResponse
{
    // Jeton JWT qui sera utilisé par le client pour les requêtes protégées
    private String token;

    // Constructeur vide nécessaire pour la sérialisation JSON
    public AuthResponse()
    {
    }

    // Constructeur pour définir le jeton directement
    public AuthResponse(String token)
    {
        this.token = token;
    }

    // Getter pour récupérer le jeton
    public String getToken()
    {
        return token;
    }

    // Setter pour modifier le jeton si besoin
    public void setToken(String token)
    {
        this.token = token;
    }
}
