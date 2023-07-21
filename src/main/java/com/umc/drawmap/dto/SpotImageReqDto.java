package com.umc.drawmap.dto;

import com.sun.istack.NotNull;
import lombok.*;

public class SpotImageReqDto {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class CreateSpotImageDto{

        @NotNull
        private Long challengeId;
        @NotNull
        private String title;
        @NotNull
        private String content;
        @NotNull
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
