package com.umc.drawmap.dto.scrap;

import lombok.*;

import java.util.List;

public class ScrapResDto {
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ScrapDto {
        private Long challengeId;
        private Long user_courseId;
    }

}
