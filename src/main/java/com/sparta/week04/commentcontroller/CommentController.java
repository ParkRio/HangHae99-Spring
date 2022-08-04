package com.sparta.week04.commentcontroller;

import com.sparta.week04.commentservice.CommentService;
import com.sparta.week04.model.Post;
import com.sparta.week04.model.Reply;
import com.sparta.week04.replydto.ReplyRequestDto;
import com.sparta.week04.replydto.ReplyResponseDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("api/{post_id}/comment")
    public ReplyResponseDto createComment(@RequestBody ReplyRequestDto replyRequestDto, @PathVariable Long post_id) {
        return commentService.createReply(replyRequestDto, post_id);
    }
}
