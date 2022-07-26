package com.rio.introduction.dto;

import com.rio.introduction.domain.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateResponseDTO {
    private String title;
    private String writer;
    private String content;

    public void update(Post post) {
        this.title = post.getTitle();
        this.writer = post.getWriter();
        this.content = post.getContent();
    }
}
