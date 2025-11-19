package com.openclassrooms.mddapi.payload.response;

import lombok.Data;

@Data
public class TopicDto {
    private Long id;
    private String name;
    private String description;
    private boolean subscribed; // Indique si l'utilisateur actuel est abonné
}