package com.sparta.week04.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class RequestDto {
    private String title;
    private String content;
    private String author;
    private String password;
}
