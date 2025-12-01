package com.openclassrooms.mddapi.dto;

/**
 * DTO de réponse générique pour les messages simples.
 * <p>
 * Utilisé pour retourner des messages de confirmation, d'erreur,
 * ou d'information au client (ex: "Abonnement réussi", "Email déjà utilisé").
 * </p>
 *
 */
public class MessageResponse
{
    /**
     * Message à afficher au client.
     */
    private String message;

    /**
     * Constructeur par défaut.
     */
    public MessageResponse()
    {
    }

    /**
     * Constructeur avec initialisation du message.
     *
     * @param message le message à retourner
     */
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
