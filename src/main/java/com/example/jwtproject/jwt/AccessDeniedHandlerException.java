package com.example.jwtproject.jwt;

import com.example.jwtproject.controller.response.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerException implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter()
                .println(
                        new ObjectMapper()
                                .writeValueAsString(
                                        ResponseDto.fail("BAD_REQUEST", "로그인이 필요합니다. AccessDeniedHandlerException")
                                )
                );

        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 error 허가 안됨
    }
}
