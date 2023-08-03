package com.umc.drawmap.security.jwt;


import com.umc.drawmap.dto.token.TokenResDto;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    @Value("spring.jwt.secret")
    private String secretKey;
    private String ROLES = "roles";
    private final Long tokenValidMillisecond = 60 * 60 * 1000L;
    private final Long accessTokenValidMillisecond = 60 * 60 * 1000L; // 1시간
    private final Long refreshTokenValidMillisecond = 14 * 24 * 60 * 60 * 1000L; // 24시간

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 생성
    public TokenResDto createToken(Long userId, List<String> roles) {
        // UserId & Role 삽입.
        Claims claims = Jwts.claims().setSubject(String.valueOf(userId));
        claims.put(ROLES, roles);

        Date now = new Date();

        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setExpiration(new Date(now.getTime() + refreshTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return TokenResDto.builder()
                .grantType("bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpireDate(accessTokenValidMillisecond)
                .build();
    }

    // JWT 를 통해 정보 조회 -> 수정 필요 (User Detail)
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        if (claims.get(ROLES) == null ) {
            throw new RuntimeException("역할 저장 X"); // 에러 다시 지정할 예정
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        System.out.println(userDetails.getAuthorities());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Jwt 토큰 복호화(vs 암호화)
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String resolveToken(HttpServletRequest request) {

        return request.getHeader("Authorization");
    }
    // JWT 의 유효성 / 만료 일자 확인
    public boolean validationToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println(e.toString());
            return false;
        }
    }


}
