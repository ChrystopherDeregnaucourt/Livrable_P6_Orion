package com.openclassrooms.mddapi.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.payload.response.UserDto;
import com.openclassrooms.mddapi.service.IUserService;

@RestController
@RequestMapping("/api/auth/me")
public class MeController {

    @Autowired
    private IUserService userService;

    // GET : Récupérer le profil de l'utilisateur connecté
    @GetMapping
    public ResponseEntity<?> getMe(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        
        // Principal.getName() retourne le username ou l'email
        // Mais ici on utilise Authentication pour récupérer l'objet UserDetails complet
        UserDetails userDetails = (UserDetails) ((Authentication) principal).getPrincipal();
        
        // On récupère l'entité User complète via le service pour avoir toutes les infos (ID, dates...)
        // Note: Dans notre UserDetailsImpl, on stocke l'ID, on pourrait l'utiliser directement
        com.openclassrooms.mddapi.security.services.UserDetailsImpl userImpl = 
            (com.openclassrooms.mddapi.security.services.UserDetailsImpl) userDetails;
            
        User user = userService.findById(userImpl.getId());
        
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Mapping manuel vers DTO
        UserDto response = new UserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );

        return ResponseEntity.ok(response);
    }

    // PUT : Mettre à jour le profil
    @PutMapping
    public ResponseEntity<?> updateMe(Principal principal, @RequestBody UserDto userDto) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        UserDetails userDetails = (UserDetails) ((Authentication) principal).getPrincipal();
        com.openclassrooms.mddapi.security.services.UserDetailsImpl userImpl = 
            (com.openclassrooms.mddapi.security.services.UserDetailsImpl) userDetails;

        User userUpdate = new User();
        userUpdate.setUsername(userDto.getUsername());
        userUpdate.setEmail(userDto.getEmail());

        User updatedUser = userService.update(userImpl.getId(), userUpdate);
        
        // Retourner le DTO mis à jour
        UserDto response = new UserDto(
            updatedUser.getId(),
            updatedUser.getUsername(),
            updatedUser.getEmail(),
            updatedUser.getCreatedAt(),
            updatedUser.getUpdatedAt()
        );

        return ResponseEntity.ok(response);
    }
}