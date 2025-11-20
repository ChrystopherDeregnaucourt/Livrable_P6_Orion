package com.openclassrooms.mddapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.payload.response.UserDto;
import com.openclassrooms.mddapi.service.IUserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private IUserService userService;

    // GET : Récupérer un utilisateur par son ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        // Notre service findById retourne null si pas trouvé (pas un Optional)
        User user = userService.findById(id);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Mapping manuel vers UserDto
        UserDto userDto = new UserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );

        return ResponseEntity.ok(userDto);
    }
}