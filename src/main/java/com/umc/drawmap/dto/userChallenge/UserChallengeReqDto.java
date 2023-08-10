package com.umc.drawmap.dto.userChallenge;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserChallengeReqDto {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class UserChallengeAddDto {
        private Long challengeId;
        private String challengeComment;
        private int challengeStar;
        private String challengeImage;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class UserChallengeUpdateDto {
        // Body -> Update 할 대상들.
        private String challengeComment;
        private int challengeStar;
        private String challengeImage;
    }



}