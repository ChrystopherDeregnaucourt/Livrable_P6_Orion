package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.AuthResponse;
import com.openclassrooms.mddapi.dto.LoginRequest;
import com.openclassrooms.mddapi.dto.MessageResponse;
import com.openclassrooms.mddapi.dto.RegisterRequest;
import com.openclassrooms.mddapi.dto.UserResponse;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.CustomUserDetails;
import com.openclassrooms.mddapi.security.JwtService;
import com.openclassrooms.mddapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour l'authentification des utilisateurs.
 * <p>
 * Expose les endpoints publics pour l'inscription, la connexion,
 * et un endpoint protégé pour récupérer les informations de
 * l'utilisateur connecté.
 * </p>
 * <p>
 * Endpoints :
 * </p>
 * <ul>
 *   <li>POST /api/auth/register - Inscription d'un nouvel utilisateur</li>
 *   <li>POST /api/auth/login - Connexion d'un utilisateur existant</li>
 *   <li>GET /api/auth/me - Récupération du profil de l'utilisateur connecté</li>
 * </ul>
 *
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController
{
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    /**
     * Constructeur avec injection des dépendances.
     *
     * @param userService           le service de gestion des utilisateurs
     * @param jwtService            le service de gestion des JWT
     * @param authenticationManager le gestionnaire d'authentification Spring Security
     * @param userRepository        le repository des utilisateurs
     */
    public AuthController(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager, UserRepository userRepository)
    {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    /**
     * Inscrit un nouvel utilisateur dans l'application.
     * <p>
     * Vérifie l'unicité de l'email et du username, hash le mot de passe,
     * crée l'utilisateur et génère un token JWT.
     * </p>
     *
     * @param request les données d'inscription (email, username, mot de passe)
     * @return 200 OK avec le token JWT si succès, 400 Bad Request sinon
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request)
    {
        try
        {
            // Crée l'utilisateur
            User user = userService.createUser(request);

            // Génère un token JWT
            String token = jwtService.generateToken(user.getEmail());

            // Retourne le token
            return ResponseEntity.ok(new AuthResponse(token));
        }
        catch (IllegalArgumentException e)
        {
            // En cas d'erreur (email ou username déjà utilisé)
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * Connecte un utilisateur existant.
     * <p>
     * Accepte email ou username comme identifiant de connexion.
     * Vérifie les credentials et génère un token JWT en cas de succès.
     * </p>
     *
     * @param request les identifiants de connexion (email/username et mot de passe)
     * @return 200 OK avec le token JWT si succès, 401 Unauthorized sinon
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request)
    {
        try
        {
            // Authentifie l'utilisateur
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmailOrUsername(), request.getPassword())
            );

            // Récupère l'email depuis les détails de l'utilisateur authentifié
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();

            // Génère un token JWT
            String token = jwtService.generateToken(email);

            return ResponseEntity.ok(new AuthResponse(token));
        }
        catch (Exception e)
        {
            return ResponseEntity.status(401).body(new MessageResponse("Identifiants invalides"));
        }
    }

    /**
     * Récupère les informations de l'utilisateur actuellement connecté.
     * <p>
     * Inclut la liste des topics auxquels l'utilisateur est abonné.
     * Nécessite une authentification JWT valide.
     * </p>
     *
     * @param userDetails les détails de l'utilisateur connecté (injecté par Spring Security)
     * @return 200 OK avec les informations utilisateur
     * @throws IllegalArgumentException si l'utilisateur n'existe pas en base
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails)
    {
        // Récupère l'utilisateur depuis la base AVEC ses abonnements
        User user = userRepository.findByEmailWithSubscriptions(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        // Convertit en DTO et retourne
        return ResponseEntity.ok(userService.toResponse(user));
    }
}
