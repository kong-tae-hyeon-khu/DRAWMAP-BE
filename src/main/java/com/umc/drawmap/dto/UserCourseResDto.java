package com.umc.drawmap.dto;

import com.umc.drawmap.domain.Region;
import lombok.*;
import org.hibernate.cache.spi.support.RegionNameQualifier;

import java.time.LocalDateTime;
import java.util.List;

public class UserCourseResDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserCourseDto{
        private Long userCourseId;

        private Boolean isScraped;

        private String title;
        private LocalDateTime createdDate;
        //        private String path; // 코스 경로
        private String difficulty;
        private String content;

        private Region area;

        private String image;
        private int scrapCount;

        private UserResDto.UserDto user; // userId, profileImg, name

    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserCourseListDto{
        private List<UserCourseDto> userCourseList;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MyUserCourseDto{
        private Long userCourseId;

        private Region area;
        private LocalDateTime createdDate;

        private String image;

        private UserResDto.UserDto user; // userId, profileImg, name

    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MyUserCourseListDto{
        private List<UserCourseResDto.MyUserCourseDto> UserCourseList;
    }

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
        private Region area;
        private UserResDto.UserDto user;
        private String image;
    }
}
