package com.rio.introduction.repository;

import com.rio.introduction.domain.Post;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class DisConnectDbPostRepositoryTest {

    PostRepository postRepository = new DisConnectDbPostRepository();

    @Test
    void save() {
//        Post post = new Post(String title, String writer String content, String password);
//        post.setTitle("rio's test");
//        post.setWriter("rio");
//        post.setContent("testcode");
//
//        postRepository.save(post);
//
//        Post result = postRepository.findById(post.getId()).get(); // Optional에서 데이터를 꺼낼떈 .get() 사용가능한데 쓰긴 좀...
//        assertThat(post).isEqualTo(result); // assertThat(내가 저장한 것 ), .isEqulaTO(저장한 값 가져오기)
    }

    @Test
    void findAll() {
    }
}