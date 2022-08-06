package com.example.jwtproject.repository;

import com.example.jwtproject.domain.Comment;
import com.example.jwtproject.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
}
