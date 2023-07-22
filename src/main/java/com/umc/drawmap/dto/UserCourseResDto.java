package com.umc.drawmap.dto;

import lombok.*;

import java.time.LocalDateTime;

public class UserCourseResDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserCourseSortDto{
        private Long userCourseId;
        private String title;
        private LocalDateTime createdDate;
        private String difficulty;
        private String content;
        private String area;
        private UserResDto.UserDto user;
        private String image;
    }
}
