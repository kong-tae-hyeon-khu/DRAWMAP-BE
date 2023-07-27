package com.umc.drawmap.dto;

import com.sun.istack.NotNull;
import lombok.*;

public class UserCourseReqDto {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class CreateUserCourseDto{
        private Long userId;
        private String userCourseTitle;
        private String sido;
        private String sgg;
        private String userCourseDifficulty;
        private String userCourseContent;
        private String userCourseComment;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class UpdateUserCourseDto{
        private String userCourseTitle;
        private String sido;
        private String sgg;
        private String userCourseDifficulty;
        private String userCourseContent;
        private String userCourseComment;
    }
}
