package com.umc.drawmap.dto.userChallenge;
import lombok.Builder;
import lombok.Getter;


public class UserChallengeResDto {
    // 등록하거나 , 업데이트 했을 때 등록한 내용을 보여주면 더 편할듯해서 만듬!
    @Builder
    @Getter
    public static class GetUserChallenge {
        private Long userId; // 유저 이름도 추가..?
        private Long challengeId; // 도전코스 이름도 추가..?
        private String challengeComment;
        private int challengeStar;
        private String challengeImage;
    }
}
