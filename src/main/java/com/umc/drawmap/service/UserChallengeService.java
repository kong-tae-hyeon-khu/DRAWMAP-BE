package com.umc.drawmap.service;

import com.umc.drawmap.domain.Challenge;
import com.umc.drawmap.domain.User;
import com.umc.drawmap.domain.UserChallenge;
import com.umc.drawmap.dto.UserChallenge.UserChallengeReqDto;
import com.umc.drawmap.repository.ChallengeRepository;
import com.umc.drawmap.repository.UserChallengeRepository;
import com.umc.drawmap.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@Service
public class UserChallengeService {
    private UserChallengeRepository userChallengeRepository;
    private ChallengeRepository challengeRepository;
    private UserRepository userRepository;
    public UserChallengeService(UserChallengeRepository userChallengeRepository, ChallengeRepository challengeRepository,
                                UserRepository userRepository) {
        this.userChallengeRepository = userChallengeRepository;
        this.challengeRepository = challengeRepository;
        this.userRepository = userRepository;
    }

    public Boolean userChallengeAdd(UserChallengeReqDto.UserChallengeAddDto dto) {
        User user = userRepository.findById(dto.getUserId()).get();
        Challenge challenge = challengeRepository.findById(dto.getChallengeId()).get();

        UserChallenge userChallenge = UserChallenge.builder()
                .challengeComment(dto.getChallengeComment())
                .challenge(challenge)
                .user(user)
                .challengeStar(dto.getChallengeStar())
                .challengeImage(dto.getChallengeImage())
                .build();

        userChallengeRepository.save(userChallenge);
        return true;
    }

}