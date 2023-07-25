package com.umc.drawmap.dto.challenge;

import com.umc.drawmap.domain.Challenge;
import com.umc.drawmap.repository.UserChallengeRepository;
import com.umc.drawmap.repository.UserRepository;
import com.umc.drawmap.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ChallengeConverter {

    private static UserChallengeRepository userChallengeRepository;
    private static UserRepository userRepository;
    private static ScrapService scrapService;

    public static ChallengeResDto.ChallengeDto toChallengeDto(Challenge challenge){

        return ChallengeResDto.ChallengeDto.builder()
                .title(challenge.getChallengeCourseTitle())
                .challengeId(challenge.getId())
                .content(challenge.getChallengeCourseContent())
                .area(challenge.getChallengeCourseArea())
                .image(challenge.getChallengeImage())
                .createdDate(challenge.getCreatedAt())
                .difficulty(challenge.getChallengeCourseDifficulty())
                .scrapCount(challenge.getScrapCount())
                .build();
    }

    public static List<ChallengeResDto.ChallengeDto> toChallengeDtoList(List<Challenge> challengeList){
        return challengeList.stream()
                .map(challenge -> toChallengeDto(challenge))
                .collect(Collectors.toList());
    }

    public static ChallengeResDto.MyChallengeDto toMyChallengeDto(Challenge challenge){

        return ChallengeResDto.MyChallengeDto.builder()
                .challengeId(challenge.getId())
                .area(challenge.getChallengeCourseArea())
                .image(challenge.getChallengeImage())
                .createdDate(challenge.getCreatedAt())
                .build();
    }

    public static List<ChallengeResDto.MyChallengeDto> toChallengeDtoMyList(List<Challenge> challengeList){
        return challengeList.stream()
                .map(challenge -> toMyChallengeDto(challenge))
                .collect(Collectors.toList());
    }

    public static Page<ChallengeResDto.ChallengeSortDto> toChallengeSortList(Page<Challenge> challengePage){

        Page<ChallengeResDto.ChallengeSortDto> challengeSortDto = challengePage.map(challenge -> ChallengeResDto.ChallengeSortDto.builder()
                .challengeId(challenge.getId())
                .area(challenge.getChallengeCourseArea())
                .title(challenge.getChallengeCourseTitle())
                .content(challenge.getChallengeCourseContent())
                .createdDate(challenge.getCreatedAt())
                .difficulty(challenge.getChallengeCourseDifficulty())
                .image(challenge.getChallengeImage())
                .build());
        return challengeSortDto;
    }


}
