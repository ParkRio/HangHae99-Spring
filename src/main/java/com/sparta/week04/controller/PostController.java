package com.sparta.week04.controller;

import com.sparta.week04.dto.PasswordDto;
import com.sparta.week04.dto.RequestDto;
import com.sparta.week04.dto.ResponseDto;
import com.sparta.week04.service.PostService;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }


    @PostMapping("/api/post")
    public ResponseDto<?> createPost(@RequestBody RequestDto requestDto) {
        return postService.createPost(requestDto);
    }

    @GetMapping("/api/post/{id}")
    public ResponseDto<?> findPost(@PathVariable Long id) {
        return postService.findPost(id);
    }

    @GetMapping("/api/post")
    public ResponseDto<?> findAllPost() {
        return postService.findAllPost();
    }

    @PutMapping("/api/post/{id}")
    public ResponseDto<?> updatePost(@PathVariable Long id, @RequestBody RequestDto requestDto) {
        return postService.updatePost(id, requestDto);
    }

    @DeleteMapping("/api/post/{id}")
    public ResponseDto<?> deletePost(@PathVariable Long id) {
        return postService.deletePost(id);
    }

    @PostMapping("/api/post/{id}")
    public ResponseDto<?> validateAuthorByPassword(@PathVariable Long id, @RequestBody PasswordDto passwordDto) {
        return postService.validateAuthorByPassword(id, passwordDto);
    }


}
