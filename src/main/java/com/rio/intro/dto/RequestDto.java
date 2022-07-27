package com.rio.intro.dto;

import com.rio.intro.domain.Board;
import com.rio.intro.service.BoardService;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter // private encapsulation을 위해 지정했기에 타 클래스에 필드를 사용하기 위해 @Getter @Setter를 필요시 넣어줌
@NoArgsConstructor
public class RequestDto {
    private String content;
    private String title;
    private String author;
    private String password;

    @Builder
    public RequestDto(String title, String author, String content, String password) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.password = password;
    }

    public Board save() {
        return Board.builder()
                .title(title)
                .author(author)
                .content(content)
                .password(password)
                .build();
    }
}
