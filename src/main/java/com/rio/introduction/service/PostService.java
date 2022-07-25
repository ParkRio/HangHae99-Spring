package com.rio.introduction.service;

import com.rio.introduction.domain.Post;
import com.rio.introduction.dto.PostDTO;
import com.rio.introduction.repository.PostRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post contentUpload(PostDTO dto) {
        Post post = new Post(dto);
        postRepository.save(post);
        System.out.println(post);
        return post;
    }

    public List<Post> allPost() {
        return postRepository.findAll();
    }


}
