package com.umc.drawmap.service;
import com.umc.drawmap.domain.Challenge;
import com.umc.drawmap.domain.User;
import com.umc.drawmap.domain.UserChallenge;
import com.umc.drawmap.dto.userChallenge.UserChallengeReqDto;
import com.umc.drawmap.exception.userChallenge.NoExistChallengeException;
import com.umc.drawmap.exception.userChallenge.NoExistUserException;
import com.umc.drawmap.repository.ChallengeRepository;
import com.umc.drawmap.repository.UserChallengeRepository;
import com.umc.drawmap.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserChallengeService {
    private final UserChallengeRepository userChallengeRepository;
    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;
    public UserChallengeService(UserChallengeRepository userChallengeRepository, ChallengeRepository challengeRepository,
                                UserRepository userRepository) {
        this.userChallengeRepository = userChallengeRepository;
        this.challengeRepository = challengeRepository;
        this.userRepository = userRepository;
    }

    public Boolean userChallengeAdd(UserChallengeReqDto.UserChallengeAddDto dto) {

        if (dto.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (dto.getChallengeId() == null) {
            throw new IllegalArgumentException("Challenge ID cannot be null");
        }



        Optional<User> userOptional = userRepository.findById(dto.getUserId());
        Optional<Challenge> challengeOptional = challengeRepository.findById(dto.getChallengeId());

        if (!userOptional.isPresent()) {
            throw new NoExistUserException("Please Check User Id");
        }

        if (!challengeOptional.isPresent()) {
            throw new NoExistUserException("Please Check Challenge Id");
        }

        User user = userOptional.get();
        Challenge challenge = challengeOptional.get();

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