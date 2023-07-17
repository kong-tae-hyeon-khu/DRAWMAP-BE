package com.umc.drawmap.dto;

import lombok.*;

public class SpotImageReqDto {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class CreateSpotImageDto{

        private String title;
        private String content;
        private String area;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class UpdateSpotImageDto{
        private String title;
        private String content;
        private String area;
    }





}
