package com.rio.introduction.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PostDTO {
    private final String title;
    private final String writer;
    private final String content;
    private final String password;
}
