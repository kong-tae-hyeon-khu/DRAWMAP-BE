package com.umc.drawmap.dto;

import lombok.*;

import javax.persistence.Column;

public class ChallengeReqDto {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class CreateChallengeDto{
        private String challengeCourseTitle;

        private String challengeCourseArea;

        private String challengeCourseDifficulty;

        private String challengeCourseContent;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class UpdateChallengeDto{
        private String challengeCourseTitle;

        private String challengeCourseArea;

        private String challengeCourseDifficulty;

        private String challengeCourseContent;

    }

}
