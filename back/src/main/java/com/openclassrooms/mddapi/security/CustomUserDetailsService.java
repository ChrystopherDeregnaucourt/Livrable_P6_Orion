package com.openclassrooms.mddapi.security;

import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service pour charger les détails d'un utilisateur depuis la base de données.
 * <p>
 * Implémente {@link UserDetailsService} pour permettre à Spring Security
 * de récupérer les utilisateurs lors de l'authentification.
 * </p>
 * <p>
 * Accepte l'authentification par email ou username.
 * </p>
 *
 */
@Service
public class CustomUserDetailsService implements UserDetailsService 
{
    private final UserRepository userRepository;

    /**
     * Constructeur avec injection du repository.
     *
     * @param userRepository le repository pour accéder aux utilisateurs
     */
    public CustomUserDetailsService(UserRepository userRepository) 
    {
        this.userRepository = userRepository;
    }

    /**
     * Charge un utilisateur par son email ou username.
     * <p>
     * Méthode appelée par Spring Security lors de l'authentification.
     * Recherche l'utilisateur dans la base via email ou username.
     * </p>
     *
     * @param username l'email ou username de l'utilisateur
     * @return les détails de l'utilisateur encapsulés dans CustomUserDetails
     * @throws UsernameNotFoundException si l'utilisateur n'existe pas
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
    {
        // On cherche l'utilisateur correspondant à l'email ou username donné
        // On lève une exception s'il n'existe pas
        User user = userRepository.findByEmailOrUsername(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        // On convertit l'entité en UserDetails exploitable par Spring Security
        return new CustomUserDetails(user);
    }
}
