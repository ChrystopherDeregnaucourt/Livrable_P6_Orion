package com.openclassrooms.mddapi.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.CommentRepository;

@Service
public class CommentService implements ICommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment create(String content, Post post, User author) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post);
        comment.setAuthor(author);
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }
}