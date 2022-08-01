package com.sparta.week04.service;

import com.sparta.week04.dto.PasswordDto;
import com.sparta.week04.dto.RequestDto;
import com.sparta.week04.dto.ResponseDto;
import com.sparta.week04.model.Post;
import com.sparta.week04.repository.MemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Service
public class PostService {

    private final MemoryRepository memoryRepository;

    @Autowired
    public PostService(MemoryRepository memoryRepository) {
        this.memoryRepository = memoryRepository;
    }

    // post 생성
    @Transactional
    public ResponseDto<?> createPost(RequestDto requestDto) {
        Post post = new Post(requestDto);
        memoryRepository.save(post); // Entity PK가 Identity일시 영속성 관리 위해 트랜잭션 전 Insert 쿼리 날림.
        return ResponseDto.success(post);
    }

    // Get 조회
    @Transactional
    public ResponseDto<?> findPost(Long id) {
        Optional<Post> optionalPost = memoryRepository.findById(id);
        if (optionalPost.isEmpty()) {
            return ResponseDto.fail("NULL_POST_ID", "post id isn't exist");
        }
        // Optional을 사용한다면 그 안에 들어있는 값은 Optional.get() 메서드를 통해 접근할 수 있습니다.
        return ResponseDto.success(optionalPost.get());
    }

    // Get 전체조회
    @Transactional
    public ResponseDto<?> findAllPost() {
        return ResponseDto.success(memoryRepository.findAllByOrderByModifiedAtDesc());
    }

    // Put post수정
    @Transactional
    public ResponseDto<?> updatePost(Long id, RequestDto requestDto) {
        Optional<Post> optionalPost = memoryRepository.findById(id);

        if (optionalPost.isEmpty()) {
            return ResponseDto.fail("NULL_POST_ID", "post id isn't exist");
        }

        Post post = optionalPost.get();
        post.updatePost(requestDto);

        return ResponseDto.success(post);
    }

    @Transactional
    public ResponseDto<?> deletePost(Long id) {
        Optional<Post> optionalPost = memoryRepository.findById(id);
        if (optionalPost.isEmpty()) {
            return ResponseDto.fail("NULL_POST_ID", "post id isn't exist");
        }

        memoryRepository.delete(optionalPost.get());

        return ResponseDto.success(true);
    }

    @Transactional
    public ResponseDto<?> validateAuthorByPassword(Long id, PasswordDto passwordDto) {
        Optional<Post> optionalPost = memoryRepository.findById(id);

        if (optionalPost.isEmpty()) {
            return ResponseDto.fail("NULL_POST_ID", "post id isn't exist");
        }

        Post post = optionalPost.get();
        String password = passwordDto.getPassword();

//        System.out.println(optionalPost.get().getPassword());
//        System.out.println(post.getPassword());
//        System.out.println(password);

        if (!post.getPassword().equals(password)) {
            return ResponseDto.fail("PASSWORD_NOT_CORRECT", "password is not correct");

        }
        return ResponseDto.success(true);
    }
}
