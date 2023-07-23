package com.umc.drawmap.dto;

import com.umc.drawmap.domain.Region;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class SpotImageResDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SpotImageDto{
        private Long spotImageId;
        private String title;
        private String content;
        private Region area;
        private String image;
        private LocalDateTime createdDate;
    }


}
