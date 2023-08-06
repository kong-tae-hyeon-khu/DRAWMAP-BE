package com.umc.drawmap.security;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data
public class KakaoUserInfoResponse {
    private Long id;
    private String connected_at;
    private Properties properties;
    private KakaoAccount kakao_account;

    @Getter
    @ToString
    public static class Properties{
        private String nickname;
    }
}