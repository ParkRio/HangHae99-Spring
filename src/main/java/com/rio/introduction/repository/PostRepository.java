package com.rio.introduction.repository;

import com.rio.introduction.domain.Post;

import java.util.List;
import java.util.Optional;

/**
 * test --> db 연결되어 있지 않은 상태에서 test 해보기 위함
 */
public interface PostRepository {
    Post save(Post post); // 게시글 저장
    List<Post> findAll(); // 게시글 전체 조회
    Optional<Post> findById(Long id); // id조회
}
