package com.example.jwtproject.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.coyote.Response;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {

    private boolean success;
    private T data;
    private Error error;

    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(true, data, null);
    }

    public static <T> ResponseDto<T> fail(String code, String message) {
        return new ResponseDto<T>(false, null, new Error(code, message));
    }

    // Todo :: 자바 문법 -> Class 안에 static Class 생성
    @Getter
    @AllArgsConstructor
    static class Error {
        private String code;
        private String message;
    }
}
