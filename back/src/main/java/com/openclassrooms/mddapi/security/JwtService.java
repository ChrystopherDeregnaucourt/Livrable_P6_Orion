package com.openclassrooms.mddapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service de gestion des jetons JWT (JSON Web Tokens).
 * <p>
 * Centralise la création, la validation et l'extraction d'informations
 * depuis les jetons JWT utilisés pour l'authentification.
 * </p>
 * <p>
 * Utilise HMAC-SHA256 pour la signature des tokens.
 * La clé secrète et la durée de validité sont injectées depuis la configuration.
 * </p>
 *
 */
@Service
public class JwtService
{
    /**
     * Clé secrète pour signer les JWT (injectée depuis application.properties).
     * Doit être stockée de manière sécurisée (variable d'environnement).
     */
    @Value("${jwt.secret}")
    private String secretKey;

    /**
     * Durée de validité du token en millisecondes.
     * Valeur par défaut : 86400000 ms (24 heures).
     */
    @Value("${jwt.expiration:86400000}")
    private long expirationMs;

    /**
     * Extrait le nom d'utilisateur (subject) du jeton JWT.
     *
     * @param token le jeton JWT à analyser
     * @return l'email/username extrait du token, ou null si le token est invalide
     */
    public String extractUsername(String token)
    {
        try
        {
            // On extrait le sujet du jeton, ce qui correspond à l'email ou username de l'utilisateur
            return extractClaim(token, Claims::getSubject);
        }
        catch (Exception e)
        {
            System.out.println("Erreur lors de l'extraction du username depuis le token: " + e.getMessage());
            return null;
        }
    }

    /**
     * Extrait une information spécifique (claim) du jeton JWT.
     *
     * @param <T>            le type de l'information à extraire
     * @param token          le jeton JWT
     * @param claimsResolver la fonction pour extraire le claim voulu
     * @return la valeur du claim extraite
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Génère un nouveau jeton JWT pour un utilisateur.
     *
     * @param username l'email ou username de l'utilisateur (utilisé comme subject)
     * @return le jeton JWT signé et encodé
     */
    public String generateToken(String username)
    {
        // On récupère la date et l'heure actuelles
        Date now = new Date();
        // On calcule la date d'expiration en ajoutant la durée prévue
        Date expiryDate = new Date(now.getTime() + expirationMs);
        // On construit le jeton en y insérant le sujet et les dates
        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Vérifie si un jeton JWT est valide pour un utilisateur donné.
     * <p>
     * Vérifie que le username correspond et que le token n'est pas expiré.
     * </p>
     *
     * @param token    le jeton JWT à valider
     * @param username l'email/username de l'utilisateur
     * @return true si le token est valide, false sinon
     */
    public boolean isTokenValid(String token, String username)
    {
        String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    /**
     * Vérifie si le jeton JWT est expiré.
     *
     * @param token le jeton JWT
     * @return true si le token est expiré, false sinon
     */
    private boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrait la date d'expiration du jeton JWT.
     *
     * @param token le jeton JWT
     * @return la date d'expiration
     */
    private Date extractExpiration(String token)
    {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrait tous les claims (informations) du jeton JWT.
     *
     * @param token le jeton JWT
     * @return les claims contenus dans le token
     * @throws RuntimeException si le token est invalide ou mal formé
     */
    private Claims extractAllClaims(String token)
    {
        try
        {
            // On parse le jeton signé pour en extraire toutes les informations
            return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Invalid JWT token: " + e.getMessage(), e);
        }
    }

    /**
     * Reconstruit la clé de signature à partir de la clé secrète.
     * <p>
     * Décode la clé Base64 et crée une clé HMAC pour l'algorithme HS256.
     * </p>
     *
     * @return la clé secrète pour signer/vérifier les JWT
     */
    private SecretKey getSigningKey()
    {
        // On décode la clé secrète pour obtenir un tableau de bytes utilisable
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // On construit une clé HMAC adaptée à l'algorithme HS256
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
