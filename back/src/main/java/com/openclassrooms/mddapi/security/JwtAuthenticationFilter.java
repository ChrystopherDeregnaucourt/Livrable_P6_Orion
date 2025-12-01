package com.openclassrooms.mddapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filtre d'authentification JWT pour Spring Security.
 * <p>
 * Intercepte chaque requête HTTP pour extraire et valider le jeton JWT
 * présent dans l'en-tête Authorization. Si le token est valide,
 * l'utilisateur est authentifié dans le contexte Spring Security.
 * </p>
 * <p>
 * Les endpoints publics (login, register) sont exclus de ce filtre.
 * </p>
 * <p>
 * Exécuté une seule fois par requête grâce à {@link OncePerRequestFilter}.
 * </p>
 *
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
    private final CustomUserDetailsService userDetailsService;

    /**
     * Service pour gérer la création et la validation des jetons JWT.
     */
    private final JwtService jwtService;

    /**
     * Constructeur avec injection des services.
     *
     * @param userDetailsService le service pour charger les utilisateurs
     * @param jwtService         le service JWT
     */
    public JwtAuthenticationFilter(CustomUserDetailsService userDetailsService, JwtService jwtService)
    {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    /**
     * Filtre exécuté pour chaque requête HTTP.
     * <p>
     * Extrait le JWT de l'en-tête Authorization, le valide, et authentifie
     * l'utilisateur dans le contexte Spring Security si le token est valide.
     * </p>
     * <p>
     * Les endpoints publics (login, register, actuator) sont exclus.
     * </p>
     *
     * @param request     la requête HTTP entrante
     * @param response    la réponse HTTP
     * @param filterChain la chaîne de filtres à continuer
     * @throws ServletException en cas d'erreur de traitement
     * @throws IOException      en cas d'erreur d'E/S
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException
    {
        // Ignorer le filtre JWT pour les endpoints publics UNIQUEMENT
        String requestPath = request.getServletPath();
        String requestURI = request.getRequestURI();
        
        // Vérifie si c'est un endpoint public (login et register, pas /me)
        if ((requestPath.equals("/api/auth/login") || requestURI.equals("/api/auth/login")) ||
            (requestPath.equals("/api/auth/register") || requestURI.equals("/api/auth/register")) ||
            requestPath.startsWith("/actuator/") || requestURI.startsWith("/actuator/"))
        {
            filterChain.doFilter(request, response);
            return;
        }
        
        // On récupère la valeur de l'en-tête Authorization
        String authHeader = request.getHeader("Authorization");

        // On prépare des variables pour le jeton et l'identifiant utilisateur
        String jwt = null;
        String username = null;

        // On vérifie que l'en-tête existe et suit le format Bearer
        if (authHeader != null && authHeader.startsWith("Bearer "))
        {
            try
            {
                // On extrait le jeton sans le préfixe
                jwt = authHeader.substring(7);
                
                // Vérification basique du format du token avant d'essayer de l'analyser
                if (jwt != null && !jwt.trim().isEmpty() && jwt.split("\\.").length == 3)
                {
                    // On extrait l'email/username stocké dans le jeton
                    username = jwtService.extractUsername(jwt);
                }
            }
            catch (Exception e)
            {
                System.out.println("Erreur lors de l'extraction JWT: " + e.getMessage());
                // On continue sans authentification en cas d'erreur
            }
        }

        // On s'assure qu'aucune authentification n'est déjà définie dans le contexte
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            try
            {
                // On charge les informations de l'utilisateur à partir de l'email/username
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // On vérifie que le jeton est bien valide
                if (jwtService.isTokenValid(jwt, userDetails.getUsername()))
                {
                    // On crée une authentification basée sur les informations de l'utilisateur
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    // On attache des détails supplémentaires à partir de la requête HTTP
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // On enregistre l'authentification dans le contexte de sécurité
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            catch (Exception e)
            {
                System.out.println("Erreur lors de l'authentification: " + e.getMessage());
            }
        }

        // On poursuit la chaîne de filtres pour laisser la requête continuer
        filterChain.doFilter(request, response);
    }
}
