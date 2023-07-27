package com.umc.drawmap.dto;

import lombok.*;

import java.time.LocalDateTime;

public class SpotImageResDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SpotImageDto{
        private Long spotImageId;
        private String title;
        private String content;
        private String sido;
        private String sgg;
        private String image;
        private LocalDateTime createdDate;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SpotImageBriefDto{
        private Long spotImageId;
        private String title;
        private String image;
    }




}
