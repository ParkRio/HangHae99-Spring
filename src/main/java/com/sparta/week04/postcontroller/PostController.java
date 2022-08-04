package com.sparta.week04.postcontroller;

import com.sparta.week04.dto.PostPasswordDto;
import com.sparta.week04.dto.PostRequestDto;
import com.sparta.week04.dto.PostResponseDto;
import com.sparta.week04.postservice.PostService;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }


    @PostMapping("/api/post")
    public PostResponseDto<?> createPost(@RequestBody PostRequestDto requestDto) {
        return postService.createPost(requestDto);
    }

    @GetMapping("/api/post/{id}")
    public PostResponseDto<?> findPost(@PathVariable Long id) {
        return postService.findPost(id);
    }

    @GetMapping("/api/post")
    public PostResponseDto<?> findAllPost() {
        return postService.findAllPost();
    }

    @PutMapping("/api/post/{id}")
    public PostResponseDto<?> updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto) {
        return postService.updatePost(id, requestDto);
    }

    @DeleteMapping("/api/post/{id}")
    public PostResponseDto<?> deletePost(@PathVariable Long id) {
        return postService.deletePost(id);
    }

    @PostMapping("/api/post/{id}")
    public PostResponseDto<?> validateAuthorByPassword(@PathVariable Long id, @RequestBody PostPasswordDto postPasswordDto) {
        return postService.validateAuthorByPassword(id, postPasswordDto);
    }


}
