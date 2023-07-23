package com.umc.drawmap.dto;

import com.umc.drawmap.domain.Region;
import lombok.*;

public class UserCourseReqDto {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class CreateUserCourseDto{
        private String userCourseTitle;
        private Region userCourseArea;
        private String userCourseDifficulty;
        private String userCourseContent;

        private Long userId;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class UpdateUserCourseDto{
        private String userCourseTitle;
        private Region userCourseArea;
        private String userCourseDifficulty;
        private String userCourseContent;
    }
}
