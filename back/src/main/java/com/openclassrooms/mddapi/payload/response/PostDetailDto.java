package com.openclassrooms.mddapi.payload.response;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PostDetailDto extends PostDto {
    private List<CommentDto> comments;
}