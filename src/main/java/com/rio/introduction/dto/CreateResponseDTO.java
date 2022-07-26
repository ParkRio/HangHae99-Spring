package com.rio.introduction.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CreateResponseDTO {
    // userdata response 용
    private final String title;
    private final String writer;
    private final String content;
}
