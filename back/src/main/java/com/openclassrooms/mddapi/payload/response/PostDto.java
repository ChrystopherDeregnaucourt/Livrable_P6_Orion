package com.openclassrooms.mddapi.payload.response;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String author; // On renvoie juste le nom de l'utilisateur
    private LocalDateTime date;
    private String theme;  // On renvoie juste le nom du thème
}