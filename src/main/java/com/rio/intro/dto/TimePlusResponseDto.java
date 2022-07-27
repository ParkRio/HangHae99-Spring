package com.rio.intro.dto;

import com.rio.intro.domain.Timestamped;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TimePlusResponseDto extends Timestamped {
    private String title;
    private String author;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    @Builder
    public TimePlusResponseDto(String title, String author, String content, LocalDateTime createAt, LocalDateTime modifiedAt) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;
    }
}
