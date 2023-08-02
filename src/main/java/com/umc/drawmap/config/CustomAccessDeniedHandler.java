package com.umc.drawmap.config;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 에러 메시지를 작성하거나 예외 정보 등을 사용하여 에러 메시지를 구성합니다.
        String errorMessage = "Access Denied: " + accessDeniedException.getMessage();

        // response에 에러 메시지를 작성합니다.
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(errorMessage);
    }
}

