package com.umc.drawmap.dto.challenge;

import com.umc.drawmap.domain.Region;
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

        private Region challengeCourseArea;

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

        private Region challengeCourseArea;

        private String challengeCourseDifficulty;

        private String challengeCourseContent;

    }

}
