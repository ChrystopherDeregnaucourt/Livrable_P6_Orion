package com.openclassrooms.mddapi.security;

import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Ce service permet à Spring Security de charger les utilisateurs depuis la base
 */
@Service
public class CustomUserDetailsService implements UserDetailsService 
{
    private final UserRepository userRepository;

    // Injecte le repository
    public CustomUserDetailsService(UserRepository userRepository) 
    {
        this.userRepository = userRepository;
    }

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
