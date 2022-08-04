package com.sparta.week04.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostResponseDto<T> {
    private boolean success;
    private T data;
    private Error error;

    // Todo 제네릭 공부
    public static <T> PostResponseDto<T> success(T data) {
        return new PostResponseDto<>(true, data, null);
    }

    public static <T> PostResponseDto<T> fail(String code, String message) {
        return new PostResponseDto<>(false, null, new Error(code, message));
    }


    // Todo Class안에 class 만들기 공부
    @Getter
    @AllArgsConstructor
    static class Error {
        private String code;
        private String message;
    }
}
