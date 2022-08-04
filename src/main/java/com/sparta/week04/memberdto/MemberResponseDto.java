package com.sparta.week04.memberdto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberResponseDto<T> {
    private boolean success;
    private T data;
    private Error error;

    public static <T> MemberResponseDto<T> success(T data) {
        return new MemberResponseDto<>(true, data, null);
    }

    public static <T> MemberResponseDto<T> fail(String code, String message) {
        return new MemberResponseDto<>(false, null, new Error(code, message));
    }

    public static <T> MemberResponseDto<T> of(T data) {
        return new MemberResponseDto<>(true, data, null);
    }

    @Getter
    @AllArgsConstructor
    static class Error {
        private String code;
        private String message;
    }
}
