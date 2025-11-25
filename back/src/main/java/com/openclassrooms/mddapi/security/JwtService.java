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
 * Ce service centralise la création et la validation des jetons JWT
 */
@Service
public class JwtService
{
    // Clé secrète injectée depuis la configuration (variable d'environnement)
    @Value("${jwt.secret}")
    private String secretKey;

    // Durée de validité injectée depuis la configuration (avec valeur par défaut de 24h)
    @Value("${jwt.expiration:86400000}")
    private long expirationMs;

    // Méthode qui extrait le sujet (l'email ou username) depuis un jeton
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

    // Méthode générique pour extraire n'importe quelle information du jeton
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        // On lit toutes les informations du jeton
        Claims claims = extractAllClaims(token);
        // On applique la fonction fournie pour récupérer l'information voulue
        return claimsResolver.apply(claims);
    }

    // Méthode qui génère un nouveau jeton à partir d'un nom d'utilisateur
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

    // Méthode qui vérifie si un jeton est encore valide pour l'utilisateur donné
    public boolean isTokenValid(String token, String username)
    {
        // On récupère l'email/username stocké dans le jeton
        String extractedUsername = extractUsername(token);
        // On compare l'email/username et on vérifie que le jeton n'est pas expiré
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    // Méthode qui vérifie si la date d'expiration est dépassée
    private boolean isTokenExpired(String token)
    {
        // On vérifie si la date d'expiration est antérieure à l'instant présent
        return extractExpiration(token).before(new Date());
    }

    // Méthode qui récupère la date d'expiration du jeton
    private Date extractExpiration(String token)
    {
        // On extrait la date d'expiration du jeton
        return extractClaim(token, Claims::getExpiration);
    }

    // Méthode qui lit toutes les informations contenues dans le jeton
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

    // Méthode qui reconstruit la clé de signature à partir de la chaîne codée en base64
    private SecretKey getSigningKey()
    {
        // On décode la clé secrète pour obtenir un tableau de bytes utilisable
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // On construit une clé HMAC adaptée à l'algorithme HS256
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
