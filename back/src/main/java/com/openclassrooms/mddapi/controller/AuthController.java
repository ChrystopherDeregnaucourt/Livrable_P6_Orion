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
 * Contrôleur pour l'authentification (inscription, connexion, infos utilisateur)
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController
{
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthController(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager, UserRepository userRepository)
    {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    /**
     * Endpoint pour l'inscription d'un nouvel utilisateur
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
     * Endpoint pour la connexion
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
     * Endpoint pour récupérer les informations de l'utilisateur connecté
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
