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
 * Configuration de la sécurité Spring Security pour l'application.
 * <p>
 * Définit :
 * </p>
 * <ul>
 *   <li>Les règles d'accès aux endpoints (publics vs protégés)</li>
 *   <li>L'authentification JWT via un filtre personnalisé</li>
 *   <li>La configuration CORS</li>
 *   <li>Le mode de session stateless (sans session serveur)</li>
 *   <li>L'encodage des mots de passe avec BCrypt</li>
 * </ul>
 * <p>
 * Les endpoints publics : /api/auth/register, /api/auth/login, /actuator/**
 * </p>
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    /**
     * Service pour charger les utilisateurs depuis la base de données.
     */
    private final CustomUserDetailsService userDetailsService;

    /**
     * Filtre JWT personnalisé pour extraire et valider les tokens.
     */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Origines autorisées pour CORS (injectées depuis application.properties).
     * Valeur par défaut : http://localhost:4200
     */
    @Value("${app.cors.allowed-origins:http://localhost:4200}")
    private String[] allowedOrigins;

    /**
     * Constructeur avec injection des dépendances.
     *
     * @param userDetailsService       le service pour charger les utilisateurs
     * @param jwtAuthenticationFilter  le filtre d'authentification JWT
     */
    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter)
    {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Configure la chaîne de filtres de sécurité HTTP.
     * <p>
     * Définit les règles d'autorisation, désactive CSRF (non nécessaire en mode stateless),
     * active CORS, configure le mode session stateless, et ajoute le filtre JWT.
     * </p>
     *
     * @param http l'objet HttpSecurity pour configurer la sécurité
     * @return la chaîne de filtres configurée
     * @throws Exception en cas d'erreur de configuration
     */
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

    /**
     * Bean pour l'encodage sécurisé des mots de passe.
     * <p>
     * Utilise BCrypt, un algorithme de hashage unidirectionnel adapté
     * pour les mots de passe.
     * </p>
     *
     * @return l'encodeur de mots de passe BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean pour le provider d'authentification basé sur la base de données.
     * <p>
     * Configure le service de chargement des utilisateurs et l'encodeur de mots de passe.
     * </p>
     *
     * @return le provider d'authentification configuré
     */
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

    /**
     * Bean pour l'AuthenticationManager de Spring Security.
     * <p>
     * Requis pour l'authentification programmatique (ex: lors du login).
     * </p>
     *
     * @param configuration la configuration d'authentification
     * @return le gestionnaire d'authentification
     * @throws Exception en cas d'erreur de configuration
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception
    {
        return configuration.getAuthenticationManager();
    }

    /**
     * Configure CORS (Cross-Origin Resource Sharing) pour autoriser les requêtes
     * depuis le front-end Angular.
     * <p>
     * En développement, accepte toutes les origines avec pattern.
     * En production, doit être configuré avec des origines spécifiques.
     * </p>
     *
     * @return la configuration CORS
     */
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
