package com.example.jwtproject.controller;

import com.example.jwtproject.controller.request.PostRequestDto;
import com.example.jwtproject.controller.response.ResponseDto;
import com.example.jwtproject.service.PostService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // Todo :: 게시글 생성 -> 게시글 requestDto , HttpServletRequest 로 accessToken, refreshToken 받기.
    @PostMapping("/post")
    public ResponseDto<?> createPost(@RequestBody PostRequestDto postRequestDto, HttpServletRequest request) {
        return postService.createPost(postRequestDto, request);
    }

    // Todo :: 게시글 조회
    @GetMapping("/post/{id}")
    public ResponseDto<?> getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    // Todo :: 게시글 전체 조회
    @GetMapping("/all-post")
    public ResponseDto<?> getAllPosts() {
        return postService.getAllPost();
    }

    // Todo :: 단건 게시글 수정
    @PutMapping("/post/{id}")
    public ResponseDto<?> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto, HttpServletRequest request) {
        return postService.updatePost(id, postRequestDto, request);
    }

    // Todo :: 게시글 삭제
    @DeleteMapping("/post/{id}")
    public ResponseDto<?> deletePost(@PathVariable Long id, HttpServletRequest request) {
        return postService.deletePost(id, request);
    }



}