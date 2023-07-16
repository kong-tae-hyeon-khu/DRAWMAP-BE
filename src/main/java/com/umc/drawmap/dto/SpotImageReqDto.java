package com.umc.drawmap.dto;

import lombok.Getter;

public class SpotImageReqDto {

    @Getter
    public static class CreateSpotImageDto{
        private String title;
        private String content;
        private String area;
    }

    @Getter
    public static class UpdateSpotImageDto{
        private String title;
        private String content;
        private String area;
    }





}
