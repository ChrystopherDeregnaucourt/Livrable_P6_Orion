package com.openclassrooms.mddapi.security;

import com.openclassrooms.mddapi.entity.User;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Adaptateur pour l'entité User vers l'interface UserDetails de Spring Security.
 * <p>
 * Permet à Spring Security d'utiliser notre entité {@link User} personnalisée
 * tout en respectant le contrat {@link UserDetails}.
 * </p>
 * <p>
 * Note : Les fonctionnalités avancées (expiration de compte, verrouillage, etc.)
 * ne sont pas implémentées pour le moment et retournent toujours true.
 * </p>
 *
 */
public class CustomUserDetails implements UserDetails
{
    /**
     * Entité utilisateur encapsulée.
     */
    private final User user;
    
    /**
     * Constructeur avec l'entité User.
     *
     * @param user l'entité utilisateur à encapsuler
     */
    public CustomUserDetails(User user)
    {
        this.user = user;
    }

    /**
     * Retourne les autorités (rôles) de l'utilisateur.
     * <p>
     * Non implémenté actuellement - retourne une liste vide.
     * </p>
     *
     * @return une collection vide d'autorités
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return Collections.emptyList();
    }

    /**
     * Retourne le mot de passe hashé de l'utilisateur.
     *
     * @return le mot de passe hashé avec BCrypt
     */
    @Override
    public String getPassword()
    {
        return user.getPassword();
    }

    /**
     * Retourne l'email de l'utilisateur (utilisé comme username).
     *
     * @return l'email qui sert d'identifiant de connexion
     */
    @Override
    public String getUsername()
    {
        return user.getEmail();
    }

    /**
     * Indique si le compte utilisateur est expiré.
     * <p>
     * Non implémenté - retourne toujours true.
     * </p>
     *
     * @return true (le compte n'expire jamais)
     */
    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    /**
     * Indique si le compte utilisateur est verrouillé.
     * <p>
     * Non implémenté - retourne toujours true.
     * </p>
     *
     * @return true (le compte n'est jamais verrouillé)
     */
    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    /**
     * Indique si les credentials de l'utilisateur sont expirés.
     * <p>
     * Non implémenté - retourne toujours true.
     * </p>
     *
     * @return true (les credentials n'expirent jamais)
     */
    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    /**
     * Indique si le compte utilisateur est activé.
     * <p>
     * Non implémenté - retourne toujours true.
     * </p>
     *
     * @return true (le compte est toujours activé)
     */
    @Override
    public boolean isEnabled()
    {
        return true;
    }

    /**
     * Retourne l'entité User encapsulée.
     *
     * @return l'entité utilisateur
     */
    public User getUser()
    {
        return user;
    }

    /**
     * Récupère l'identifiant de l'utilisateur.
     *
     * @return l'ID de l'utilisateur
     */
    public Long getId()
    {
        return user.getId();
    }
}
