package com.openclassrooms.mddapi.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.openclassrooms.mddapi.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Récupérer les commentaires d'un article, souvent triés par date
    List<Comment> findByPostId(Long postId);
}