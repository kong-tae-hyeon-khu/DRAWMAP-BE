package com.umc.drawmap.dto.user;


import com.umc.drawmap.domain.Gender;
import com.umc.drawmap.domain.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserReqDto {
    // 유저 가입 정보
    @Setter
    @Getter
    @Builder
    public static class signUpDto {
        private String nickName;
        private String email;
        private String profileImg;
        private String bike;
        private Gender gender;
        private Role role;

        private String sido;
        private String sgg;
    }


    // 유저 기본 정보(이름 , 프로필, 자전거 , 지역) 업데이트
    @Setter
    @Getter
    @NoArgsConstructor
    public static class updateDto {
        private String nickName;
        private String profileImg;
        private String bike;
        private String sido;
        private String sgg;

    }
}
