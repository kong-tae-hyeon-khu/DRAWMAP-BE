package com.umc.drawmap.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserReqDto {
    // 유저 기본 정보(이름 , 프로필, 자전거 , 지역) 업데이트
    @Setter
    @Getter
    @NoArgsConstructor
    public static class updateDto {
        private String nickName;
        private String profileImg;
        private String bike;
        private Region region;

    }
}
