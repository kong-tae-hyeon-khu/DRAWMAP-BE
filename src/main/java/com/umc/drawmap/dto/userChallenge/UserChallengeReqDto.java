package com.umc.drawmap.dto.userChallenge;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/*
    private User userId;

    @Column(name = "challenge_course_id")
    @OneToOne
    private Challenge challengeCourseId;

    @Column(name = "challenge_comment")
    private String challengeComment;

    @Column(name = "challenge_star")
    private int challengeStar;

    @Column(name = "challenge_image")
    private String challengeImage;
* */
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
