package com.umc.drawmap.dto;

import lombok.*;

public class UserCourseReqDto {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class CreateUserCourseDto{
        private String userCourseTitle;
        private String userCourseArea;
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
        private String userCourseArea;
        private String userCourseDifficulty;
        private String userCourseContent;
    }
}
