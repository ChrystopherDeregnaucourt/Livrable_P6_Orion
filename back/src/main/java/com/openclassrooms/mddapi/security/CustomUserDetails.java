package com.openclassrooms.mddapi.security;

import com.openclassrooms.mddapi.entity.User;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Cette classe adapte notre entité User au format attendu par Spring Security
 * 
 * Les méthodes suivantes ne sont pas encore gérées (A faire si j'ai le temps :) )
 * - isAccountNonExpired
 * - isAccountNonLocked
 * - isCredentialsNonExpired
 * - isEnabled
 */
public class CustomUserDetails implements UserDetails
{
    // Entity user
    private final User user;
    
    public CustomUserDetails(User user)
    {
        this.user = user;
    }

    // Méthode qui retourne les rôles (vide pour le moment)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        // Pas géré
        return Collections.emptyList();
    }

    @Override
    public String getPassword()
    {
        // On renvoie le mot de passe chiffré de l'utilisateur
        return user.getPassword();
    }

    @Override
    public String getUsername()
    {
        // On renvoie l'email qui sert d'identifiant de connexion
        return user.getEmail();
    }

    // Méthode qui indique si le compte est expiré
    @Override
    public boolean isAccountNonExpired()
    {
        // Pas géré
        return true;
    }

    // Méthode qui indique si le compte est verrouillé
    @Override
    public boolean isAccountNonLocked()
    {
        // Pas géré
        return true;
    }

    // Méthode qui indique si les identifiants ont expiré
    @Override
    public boolean isCredentialsNonExpired()
    {
        // Pas géré
        return true;
    }

    // Méthode qui indique si le compte est activé
    @Override
    public boolean isEnabled()
    {
        // Pas géré
        return true;
    }

    public User getUser()
    {
        return user;
    }

    /**
     * Récupère l'ID de l'utilisateur
     */
    public Long getId()
    {
        return user.getId();
    }
}
