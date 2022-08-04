package com.sparta.week04.replydto;

import com.sparta.week04.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReplyResponseDto {
    private Long id;
    private String comment;

    public ReplyResponseDto(Long id, String comment) {
        this.id = id;
        this.comment = comment;
    }
}
