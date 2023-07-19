package com.umc.drawmap.dto.userChallenge;
import lombok.Builder;
import lombok.Getter;


public class UserChallengeResDto {
    // 등록하거나 , 업데이트 했을 때 등록한 내용을 보여주면 더 편할듯.
    @Builder
    @Getter
    public static class GetUserChallenge {
        private Long userId;
        private Long challengeId;
        private String challengeComment;
        private int challengeStar;
        private String challengeImage;
    }
}
