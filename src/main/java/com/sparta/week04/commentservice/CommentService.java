package com.sparta.week04.commentservice;

import com.sparta.week04.model.Post;
import com.sparta.week04.model.Reply;
import com.sparta.week04.replydto.ReplyRequestDto;
import com.sparta.week04.replydto.ReplyResponseDto;
import com.sparta.week04.repository.CommetRepository;
import com.sparta.week04.repository.MemoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class CommentService {

    private final CommetRepository commetRepository;
    private final MemoryRepository memoryRepository;

    public CommentService(CommetRepository commetRepository, MemoryRepository memoryRepository) {
        this.commetRepository = commetRepository;
        this.memoryRepository = memoryRepository;
    }

    @Transactional
    public ReplyResponseDto createReply(ReplyRequestDto replyRequestDto, Long post_id) {
        Post post = memoryRepository.findById(post_id).orElseThrow(() -> new IllegalStateException("123456789"));
        Reply reply = new Reply(replyRequestDto, post);
        commetRepository.save(reply);
        return new ReplyResponseDto(reply.getId(), reply.getComment());


    }
}
