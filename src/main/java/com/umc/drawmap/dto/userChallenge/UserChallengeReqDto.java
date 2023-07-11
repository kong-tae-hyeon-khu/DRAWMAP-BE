package com.umc.drawmap.dto.userChallenge;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserChallengeReqDto {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class UserChallengeAddDto {
        private Long userId;
        private Long challengeId;
        private String challengeComment;
        private int challengeStar;
        private String challengeImage;
    }
}