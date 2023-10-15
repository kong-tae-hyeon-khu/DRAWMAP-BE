package com.umc.drawmap.dto.user;


import com.umc.drawmap.domain.Role;
import lombok.*;

import java.time.LocalDateTime;

public class UserResDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserDto{
        private Long userId;
        private String profileImg;
        private String nickName;

    }

    @Builder
    @Getter
    public static class UserNameDto {
        private String nickName;
        private String message;
    }
    @Builder
    @Getter
    public static class UserEmailDto {
        private String email;
        private String message;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PostLoginDto{
        private String tokenType;
        private String accessToken;
        private Integer expiresIn;
        private String refreshToken;
        private Integer refreshTokenExpiresIn;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PostSignDto{
        private String tokenType;
        private String accessToken;
        private String nickName;
        private String email;
        private Role role;
        private String bike;
        private String profileImg;
    }
}
