package com.openclassrooms.mddapi.service;

import java.util.List;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.User;

public interface IPostService {
    List<Post> findAll();
    Post findById(Long id);
    Post create(String title, String content, Long topicId, User author);
}