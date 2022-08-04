package com.sparta.week04.postservice;

import com.sparta.week04.dto.PostPasswordDto;
import com.sparta.week04.dto.PostRequestDto;
import com.sparta.week04.dto.PostResponseDto;
import com.sparta.week04.model.Post;
import com.sparta.week04.repository.MemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public PostResponseDto<?> createPost(PostRequestDto requestDto) {
        Post post = new Post(requestDto);
        memoryRepository.save(post); // Entity PK가 Identity일시 영속성 관리 위해 트랜잭션 전 Insert 쿼리 날림.
        return PostResponseDto.success(post);
    }

    // Get 조회
    @Transactional
    public PostResponseDto<?> findPost(Long id) {
        Optional<Post> optionalPost = memoryRepository.findById(id);
        if (optionalPost.isEmpty()) {
            return PostResponseDto.fail("NULL_POST_ID", "post id isn't exist");
        }
        // Optional을 사용한다면 그 안에 들어있는 값은 Optional.get() 메서드를 통해 접근할 수 있습니다.
        return PostResponseDto.success(optionalPost.get());
    }

    // Get 전체조회
    @Transactional
    public PostResponseDto<?> findAllPost() {
        return PostResponseDto.success(memoryRepository.findAllByOrderByModifiedAtDesc());
    }

    // Put post수정
    @Transactional
    public PostResponseDto<?> updatePost(Long id, PostRequestDto requestDto) {
        Optional<Post> optionalPost = memoryRepository.findById(id);

        if (optionalPost.isEmpty()) {
            return PostResponseDto.fail("NULL_POST_ID", "post id isn't exist");
        }

        Post post = optionalPost.get();
        post.updatePost(requestDto);

        return PostResponseDto.success(post);
    }

    @Transactional
    public PostResponseDto<?> deletePost(Long id) {
        Optional<Post> optionalPost = memoryRepository.findById(id);
        if (optionalPost.isEmpty()) {
            return PostResponseDto.fail("NULL_POST_ID", "post id isn't exist");
        }

        memoryRepository.delete(optionalPost.get());

        return PostResponseDto.success(true);
    }

    @Transactional
    public PostResponseDto<?> validateAuthorByPassword(Long id, PostPasswordDto postPasswordDto) {
        Optional<Post> optionalPost = memoryRepository.findById(id);

        if (optionalPost.isEmpty()) {
            return PostResponseDto.fail("NULL_POST_ID", "post id isn't exist");
        }

        Post post = optionalPost.get();
        String password = postPasswordDto.getPostPassword();

//        System.out.println(optionalPost.get().getPassword());
//        System.out.println(post.getPassword());
//        System.out.println(password);

        if (!post.getPostPassword().equals(password)) {
            return PostResponseDto.fail("PASSWORD_NOT_CORRECT", "password is not correct");

        }
        return PostResponseDto.success(true);
    }
}
