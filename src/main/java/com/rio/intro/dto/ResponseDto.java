package com.rio.intro.dto;

import com.rio.intro.domain.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseDto {
    private String title;
    private String author;
    private String content;

    @Builder
    public ResponseDto(String title, String author, String content) {
        this.title = title;
        this.author = author;
        this.content = content;
    }
}
