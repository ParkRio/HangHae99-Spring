package com.rio.introduction.repository;

import com.rio.introduction.domain.Post;
import com.rio.introduction.dto.ContentResponseDTO;
import com.rio.introduction.dto.CreateResponseDTO;

import java.util.List;
import java.util.Optional;

/**
 * test --> db 연결되어 있지 않은 상태에서 test 해보기 위함
 */
public interface PostRepository {
    CreateResponseDTO save(Post post); // 게시글 저장

    Post findByUpdateId(Long id);

    List<ContentResponseDTO> findAll(); // 게시글 전체 조회
    List<ContentResponseDTO> findById(Long id); // id조회

    String checkPassword(Long id, String pwd); // 비밀번호 조회

    String deletePost(Long id); // 삭제

}
