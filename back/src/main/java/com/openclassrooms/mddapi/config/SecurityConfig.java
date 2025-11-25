package com.openclassrooms.mddapi.config;

import com.openclassrooms.mddapi.security.CustomUserDetailsService;
import com.openclassrooms.mddapi.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;

/**
 * Cette classe configure la sécurité HTTP et l'authentification
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    // Service qui charge les utilisateurs depuis la base
    private final CustomUserDetailsService userDetailsService;

    // Filtre qui gère l'extraction et la validation des jetons JWT
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${app.cors.allowed-origins:http://localhost:4200}")//Valeur par défaut pour dev pour l'origine
    private String[] allowedOrigins;

    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter)
    {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // Définit comment les requêtes HTTP sont sécurisées
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http.csrf(csrf -> csrf.disable())
                // On active CORS avec notre configuration personnalisée
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Configuration des autorisations par endpoint
                .authorizeHttpRequests(authorize -> authorize
                        // Routes publiques - pas d'authentification requise
                        .requestMatchers("/api/auth/register", "/api/auth/login", "/api/auth/test", "/api/auth/env-check").permitAll()
                        // Endpoints de monitoring publics
                        .requestMatchers("/actuator/**").permitAll()
                        // Toutes les autres routes nécessitent une authentification
                        .anyRequest().authenticated())
                // On configure le provider d'authentification
                .authenticationProvider(authenticationProvider())
                // On passe en mode stateless car on s'appuie sur JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // On ajoute notre filtre JWT avant le filtre d'authentification standard
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                
        // On construit et on renvoie la configuration de sécurité
        return http.build();
    }

    // Chiffrement des mots de passe
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        // On utilise BCrypt pour hacher les mots de passe de manière sécurisée
        return new BCryptPasswordEncoder();
    }

    // Bean qui relie le service utilisateur à l'authentification Spring
    @Bean
    @SuppressWarnings("deprecation")
    public DaoAuthenticationProvider authenticationProvider()
    {
        // On utilise l'approche traditionnelle car apparemment Spring Security 7 n'est pas encore totalement compatible
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        // On indique au provider comment charger les utilisateurs
        provider.setUserDetailsService(userDetailsService);

        // On lui indique comment vérifier les mots de passe
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    // Bean qui expose l'AuthenticationManager nécessaire pour la connexion
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception
    {
        // On délègue à Spring la création de l'AuthenticationManager
        return configuration.getAuthenticationManager();
    }

    // Configuration CORS pour autoriser les requêtes depuis différentes origines
    @Bean
    public CorsConfigurationSource corsConfigurationSource()
    {
        CorsConfiguration configuration = new CorsConfiguration();

        // En mode développement, on accepte toutes les origines pour faciliter les tests avec Postman
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));

        // Autoriser uniquement les méthodes réellement utilisées
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Accepter tous les headers
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Pas de credentials avec wildcard
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);//enregistre sur toutes les routes
        return source;
    }
}