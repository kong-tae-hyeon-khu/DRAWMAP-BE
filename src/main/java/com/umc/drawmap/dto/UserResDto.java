package com.umc.drawmap.dto;


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
}
