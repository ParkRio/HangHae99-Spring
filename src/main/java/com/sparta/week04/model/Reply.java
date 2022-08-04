package com.sparta.week04.model;

import com.sparta.week04.replydto.ReplyRequestDto;
import com.sparta.week04.replydto.ReplyResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Optional;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Reply extends Timestamped{

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Post post;

    @Builder
    public Reply(ReplyRequestDto replyRequestDto, Post post) {
        this.id = replyRequestDto.getId();
        this.comment = replyRequestDto.getComment();
        this.post = post;
    }
}
