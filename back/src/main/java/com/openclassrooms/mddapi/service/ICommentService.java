package com.openclassrooms.mddapi.service;

import java.util.List;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.User;

public interface ICommentService {
    Comment create(String content, Post post, User author);
    List<Comment> findByPostId(Long postId);
}