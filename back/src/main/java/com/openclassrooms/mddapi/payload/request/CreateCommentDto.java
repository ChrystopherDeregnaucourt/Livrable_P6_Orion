package com.openclassrooms.mddapi.payload.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCommentDto {
    @NotBlank
    private String content;
}