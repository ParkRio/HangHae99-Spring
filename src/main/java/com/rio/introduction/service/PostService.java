package com.rio.introduction.service;

import com.rio.introduction.domain.Post;
import com.rio.introduction.dto.ContentResponseDTO;
import com.rio.introduction.dto.CreateResponseDTO;
import com.rio.introduction.dto.PostDTO;
import com.rio.introduction.dto.UpdateResponseDTO;
import com.rio.introduction.repository.PostRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public CreateResponseDTO contentUpload(PostDTO dto) {
        Post post = new Post(dto); // Todo 이걸 객체로 넣으면 알아서 내용이 붙나?
        return postRepository.save(post);
    }

    public List<ContentResponseDTO> allPost() {
        return postRepository.findAll();
    }

    public List<ContentResponseDTO> findContent(Long id) {
        return postRepository.findById(id);
    };

    public String checkPwd(Long id, String pwd) {
        return postRepository.checkPassword(id, pwd);
    }

    public String deletePost(Long id) {
        return postRepository.deletePost(id);
    }

    public UpdateResponseDTO updatePost(Long id, PostDTO postDTO) {
        Post post = postRepository.findByUpdateId(id);
        post.update(postDTO);
        UpdateResponseDTO updateResponseDTO = new UpdateResponseDTO();
        updateResponseDTO.update(post);
        return updateResponseDTO;
    }
}
