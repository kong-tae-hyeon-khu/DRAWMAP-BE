package com.umc.drawmap.dto.scrap;

import lombok.*;

import java.util.List;

public class ScrapResDto {

    @Builder
    public static class BaseDto {
        private Long userId;
        private Long challengeId;
        private Long user_courseId;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ScrapDto {
        private Long userId; // 유저 아이디
        private Long challengeId; // 스크랩한 도전 코스 아이디
        private Long user_courseId; // 스크랩한 유저 코스 아이디

        private String message; // 메시지.
    }

    @Builder
    @Getter
    public static class ScrapListDto {
        private List<BaseDto> baseDtoList;
        private String message;
    }

}
