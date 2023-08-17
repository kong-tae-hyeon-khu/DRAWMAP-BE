package com.umc.drawmap.dto.challenge;

import com.umc.drawmap.domain.Difficulty;
import lombok.*;

public class ChallengeReqDto {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class CreateChallengeDto{
        private String challengeCourseTitle;

        private String sido;
        private String sgg;

        private Difficulty challengeCourseDifficulty;

        private String challengeCourseContent;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class UpdateChallengeDto{
        private String challengeCourseTitle;

        private String sido;
        private String sgg;

        private Difficulty challengeCourseDifficulty;

        private String challengeCourseContent;

    }

}
