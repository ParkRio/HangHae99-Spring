package com.rio.introduction.controller;

import com.rio.introduction.domain.Post;
import com.rio.introduction.dto.ContentResponseDTO;
import com.rio.introduction.dto.CreateResponseDTO;
import com.rio.introduction.dto.PostDTO;
import com.rio.introduction.dto.UpdateResponseDTO;
import com.rio.introduction.service.PostService;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/api/create-post") // Todo 게시글 저장
    public CreateResponseDTO createPost(@RequestBody PostDTO dto) {
        return postService.contentUpload(dto);
    }

    @GetMapping("/api/all-search-post") // Todo 게시글 전체 조회
    public List<ContentResponseDTO> searchAllPost() {
        return postService.allPost();
    }

    @GetMapping("/api/search-post/{id}") // Todo 게시글 id 단건 조회
    public List<ContentResponseDTO> searchPost(@PathVariable Long id) {
        return postService.findContent(id);
    }

    @PostMapping("/api/check-password/{id}") // Todo 게시글 패스워치 일치 조회
    public String enterPassword(@PathVariable Long id, @RequestParam String pwd) {
        return postService.checkPwd(id, pwd);
    }
    @DeleteMapping("/api/delete-post/{id}")
    public String deletePost(@PathVariable Long id) {
        return postService.deletePost(id);
    }

    @PutMapping("/api/update-post/{id}")
    public UpdateResponseDTO updatePost(@PathVariable Long id, @RequestBody PostDTO postDTO) {
        return postService.updatePost(id, postDTO);
    }
}
