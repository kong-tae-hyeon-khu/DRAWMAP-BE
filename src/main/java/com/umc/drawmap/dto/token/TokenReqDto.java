package com.umc.drawmap.dto.token;


import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class TokenReqDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class accessReqDto{
        private String access_token;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class tokenReqDto{
        private String access_token;
        private String refresh_token;
    }
}
