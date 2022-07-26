package com.rio.introduction.dto;

import com.rio.introduction.domain.Post;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class ContentResponseDTO {
    private final String title;
    private final String writer;
    private final String content;
    private final LocalDateTime createAt;
    private final LocalDateTime modifiedAt;

}
