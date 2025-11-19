package com.openclassrooms.mddapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;

@Service
public class PostService implements IPostService {
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private TopicRepository topicRepository;

    @Override
    public List<Post> findAll() {
        // On trie par date de création décroissante (du plus récent au plus ancien)
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Override
    public Post findById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public Post create(String title, String content, Long topicId, User author) {
        Topic topic = topicRepository.findById(topicId).orElse(null);
        if (topic == null) {
            return null;
        }

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setTopic(topic);
        post.setAuthor(author);
        
        return postRepository.save(post);
    }
}