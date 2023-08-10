package com.umc.drawmap.security;

import com.umc.drawmap.security.jwt.JwtProvider;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtAuthenticationFilter extends GenericFilter {
    private final JwtProvider jwtProvider;
    private final RedisTemplate redisTemplate;
    public JwtAuthenticationFilter(JwtProvider jwtProvider, RedisTemplate redisTemplate) {
        this.jwtProvider = jwtProvider;
        this.redisTemplate = redisTemplate;
    }

    // Request 로 들어오는 JWT 의 유효성 검증
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        // request 로부터 token 가져오기.
        String token = jwtProvider.resolveToken((HttpServletRequest) request);

        // 검증

        if (token != null && jwtProvider.validationToken(token)) {

            // 로그인 후, Redis 에 저장된 Access_Token 도 함께 조회 (상태 관리)
            Authentication authentication = jwtProvider.getAuthentication(token);
            String userId = ((UserDetails)authentication.getPrincipal()).getUsername();
            String key = "RT:" + userId;
            String storedToken = redisTemplate.opsForValue().get(key).toString();

            if (redisTemplate.hasKey(key) && storedToken != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }
        filterChain.doFilter(request, response);
    }
}
