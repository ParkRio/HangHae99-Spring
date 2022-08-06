package com.example.jwtproject.service;

import com.example.jwtproject.controller.request.CommentRequestDto;
import com.example.jwtproject.controller.response.CommentResponseDto;
import com.example.jwtproject.controller.response.ResponseDto;
import com.example.jwtproject.domain.Comment;
import com.example.jwtproject.domain.Member;
import com.example.jwtproject.domain.Post;
import com.example.jwtproject.jwt.TokenProvider;
import com.example.jwtproject.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TokenProvider tokenProvider;
    private final PostService postService;

    public CommentService(CommentRepository commentRepository, TokenProvider tokenProvider, PostService postService) {
        this.commentRepository = commentRepository;
        this.tokenProvider = tokenProvider;
        this.postService = postService;
    }

    // Todo :: 댓글 생성
    @Transactional
    public ResponseDto<?> createComment(CommentRequestDto commentRequestDto, HttpServletRequest request) {

        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다. Refresh-Token");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOR_FOUND", "로그인이 필요합니다. Authorization");
        }

        Member member = validateMember(request); // 회원이 맞는지 인증 검사, 회원이 맞으면 회원 정보 Member 를 되돌려줌, 밑에 메소드 구현되어 있음.

        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = postService.isPresentPost(commentRequestDto.getPostId());  // 어떤 post 인지 post 정보를 가지고 옴, postId 는 받은 것

        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        Comment comment = Comment.builder()
                .member(member)
                .post(post)
                .content(commentRequestDto.getContent())
                .build();

        commentRepository.save(comment);

        return ResponseDto.success(
                CommentResponseDto.builder()
                        .id(comment.getId())
                        .author(comment.getMember().getNickname())
                        .content(comment.getContent())
                        .createAt(comment.getCreateAt())
                        .modifiedAt(comment.getModifiedAt())
                        .build()
        );
    }

    // Todo :: 댓글 전체 조회
    @Transactional(readOnly = true)
    public ResponseDto<?> getAllCommentsByPost(Long postId) {

        Post post = postService.isPresentPost(postId);

        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        List<Comment> commentList = commentRepository.findAllByPost(post);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

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

        return ResponseDto.success(commentResponseDtoList);
    }

    // Todo :: 댓글 수정
    @Transactional
    public ResponseDto<?> updateComment(Long id, CommentRequestDto commentRequestDto, HttpServletRequest request) {

        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다. Refresh-Token");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOR_FOUND", "로그인이 필요합니다. Authorization");
        }

        Member member = validateMember(request); // 회원이 맞는지 인증 검사, 회원이 맞으면 회원 정보 Member 를 되돌려줌, 밑에 메소드 구현되어 있음.

        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = postService.isPresentPost(commentRequestDto.getPostId());  // 어떤 post 인지 post 정보를 가지고 옴, postId 는 받은 것

        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        Comment comment = isPresentComment(id); // comment 정보를 가지고 옴

        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        if (comment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        comment.update(commentRequestDto); // Comment update 메서드

        return ResponseDto.success(
                CommentResponseDto.builder()
                        .id(comment.getId())
                        .author(comment.getMember().getNickname())
                        .content(comment.getContent())
                        .createAt(comment.getCreateAt())
                        .modifiedAt(comment.getModifiedAt())
                        .build()
        );
    }

    // Todo :: 댓글 삭제
    @Transactional
    public ResponseDto<?> deleteComment(Long id, HttpServletRequest request) {

        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다. Refresh-Token");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOR_FOUND", "로그인이 필요합니다. Authorization");
        }

        Member member = validateMember(request); // 회원이 맞는지 인증 검사, 회원이 맞으면 회원 정보 Member 를 되돌려줌, 밑에 메소드 구현되어 있음.

        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Comment comment = isPresentComment(id); // comment 정보를 가지고 옴

        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        if (comment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        commentRepository.delete(comment);

        return ResponseDto.success("success");
    }


    /* ==================================================================================================== */

    @Transactional
    public Member validateMember(HttpServletRequest request) {

        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }

        return tokenProvider.getMemberFromAuthentication();
    }

    @Transactional(readOnly = true)
    public Comment isPresentComment(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        return optionalComment.orElse(null);
    }
}
