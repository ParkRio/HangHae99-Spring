package com.sparta.week04.replydto;

import com.sparta.week04.model.Post;
import lombok.Getter;

@Getter
public class ReplyRequestDto {
    private Long id;
    private String comment;
}
