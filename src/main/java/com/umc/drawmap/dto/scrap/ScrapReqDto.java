package com.umc.drawmap.dto.scrap;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ScrapReqDto {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class ScrapAddDto {
        private Long user_course_id;
        private Long challenge_id;
    }
}
