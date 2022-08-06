package com.example.jwtproject.service;

import com.example.jwtproject.controller.request.PostRequestDto;
import com.example.jwtproject.controller.response.CommentResponseDto;
import com.example.jwtproject.controller.response.PostResponseDto;
import com.example.jwtproject.controller.response.ResponseDto;
import com.example.jwtproject.domain.Comment;
import com.example.jwtproject.domain.Member;
import com.example.jwtproject.domain.Post;
import com.example.jwtproject.jwt.TokenProvider;
import com.example.jwtproject.repository.CommentRepository;
import com.example.jwtproject.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final TokenProvider tokenProvider;

    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, TokenProvider tokenProvider, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.tokenProvider = tokenProvider;
        this.commentRepository = commentRepository;
    }

    // Todo :: 게시글 생성
    @Transactional
    public ResponseDto<?> createPost(PostRequestDto postRequestDto, HttpServletRequest request) {

        if (null == request.getHeader("Refresh-Token")) { // Todo :: todo 에서 response 로 보낸 "Refresh-Token" 을 가져옴
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다. NOT Refresh-Token");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다. Not Authorization");
        }

        Member member = validateMember(request);

        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = Post.builder()
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .member(member)
                .build();

        postRepository.save(post);

        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .author(post.getMember().getNickname())
                        .createAt(post.getCreateAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        );
    }

    // Todo :: 게시글 단건 조회
    @Transactional(readOnly = true)
    public ResponseDto<?> getPost(Long id) {

        Post post = isPresentPost(id); // 게시글 찾아오는 메소드, 밑에 구현되어있음.

        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        List<Comment> commentList = commentRepository.findAllByPost(post); // post fk 를 가지고 해당되는 comment 를 가져옴
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>(); // comment 가 없을 수 있기에 초기화를 여기서 해줌

        for (Comment comment : commentList) {
            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .id(comment.getId())
                            .author(comment.getMember().getNickname())
                            .content(comment.getContent())
                            .createAt(comment.getCreateAt())
                            .modifiedAt(comment.getModifiedAt())
                            .build()
            );
        }

        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .commentResponseDtoList(commentResponseDtoList)
                        .author(post.getMember().getNickname())
                        .createAt(post.getCreateAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        );
    }

    // Todo :: 게시글 전체 조회
    @Transactional(readOnly = true)
    public ResponseDto<?> getAllPost() {
        return ResponseDto.success(postRepository.findAllByOrderByModifiedAtDesc());
    }


    // Todo :: 게시글 수정
    @Transactional
    public ResponseDto<Post> updatePost(Long id, PostRequestDto postRequestDto, HttpServletRequest request) {

        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다. Refresh-Token");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다. Authorization");
        }

        Member member = validateMember(request); // 토큰 인증 검사 후 return Member 한 것을 가지고 옴

        if (null == member) {
            return ResponseDto.fail("INVAILD_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = isPresentPost(id); // 데이터베이스에서 Post 조회해오기

        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        if (post.validateMember(member)) { // 지금 현재 로그인 중인 member 와 데이터베이스에서 조회한 post 를 작성한 member 가 같은지 조회
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        post.update(postRequestDto); // 게시글 Entity 수정 -> 트랜잭션으로 메소드가 끝나면 자동 update 쿼리가 날라감.

        return ResponseDto.success(post);
    }

    // Todo :: 게시글 삭제
    @Transactional
    public ResponseDto<?> deletePost(Long id, HttpServletRequest request) {

        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다. Refresh-Token");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다. Authorization");
        }

        Member member = validateMember(request); // 토큰 인증 검사 후 return Member 한 것을 가지고 옴

        if (null == member) {
            return ResponseDto.fail("INVAILD_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = isPresentPost(id); // 데이터베이스에서 Post 조회해오기

        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        if (post.validateMember(member)) { // 지금 현재 로그인 중인 member 와 데이터베이스에서 조회한 post 를 작성한 member 가 같은지 조회
            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);

        return ResponseDto.success("delete success");
    }
    /* ===================================================================================================== */

    // Todo :: 토큰 인증 검사
    @Transactional
    public Member validateMember(HttpServletRequest request) {

        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }

        return tokenProvider.getMemberFromAuthentication();
    }

    // Todo :: 데이터베이스에서 Post 조회해오기
    @Transactional(readOnly = true)
    public Post isPresentPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }
}
