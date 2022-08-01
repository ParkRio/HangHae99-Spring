package com.sparta.week04.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private boolean success;
    private T data;
    private Error error;

    // Todo 제네릭 공부
    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(true, data, null);
    }

    public static <T> ResponseDto<T> fail(String code, String message) {
        return new ResponseDto<>(false, null, new Error(code, message));
    }


    // Todo Class안에 class 만들기 공부
    @Getter
    @AllArgsConstructor
    static class Error {
        private String code;
        private String message;
    }
}
