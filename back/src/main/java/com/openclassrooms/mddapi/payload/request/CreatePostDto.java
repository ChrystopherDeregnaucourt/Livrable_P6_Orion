package com.openclassrooms.mddapi.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePostDto {
    @NotBlank
    private String title;
    
    @NotBlank
    private String content;
    
    @NotNull
    private Long themeId;
}