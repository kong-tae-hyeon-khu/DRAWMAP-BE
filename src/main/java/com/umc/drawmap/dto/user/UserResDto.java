package com.umc.drawmap.dto.user;


import lombok.*;

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
}
