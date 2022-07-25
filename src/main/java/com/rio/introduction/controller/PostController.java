package com.rio.introduction.controller;

import com.rio.introduction.domain.Post;
import com.rio.introduction.dto.PostDTO;
import com.rio.introduction.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/api/post-create")
    public Post createPost(@RequestBody PostDTO dto) {
        return postService.contentUpload(dto);
    }

    @GetMapping("/api/all-post")
    public List<Post> searchPost() {
        return postService.allPost();
    }
}
