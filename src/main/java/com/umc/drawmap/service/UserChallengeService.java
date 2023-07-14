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

import java.util.List;
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
    // 유저 도전 추가!
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
    // 유저 도전 수정!
    public Boolean userChallengeUpdate(Long userId, Long challengeId ,UserChallengeReqDto.UserChallengeUpdateDto dto) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Challenge> challengeOptional = challengeRepository.findById(challengeId);

        if (!userOptional.isPresent()) {
            throw new NoExistUserException("Please Check User Id");
        }

        if (!challengeOptional.isPresent()) {
            throw new NoExistUserException("Please Check Challenge Id");
        }


        Optional<UserChallenge> userChallengeOptional = userChallengeRepository.findUserChallengeByUserAndChallenge(
                userOptional.get(), challengeOptional.get()
        );

        if (!userChallengeOptional.isPresent()) {
            throw new IllegalArgumentException("해당 유저와 코스에 일치하는 데이터가 존재하지 않습니다.");
        }

        UserChallenge userChallenge = userChallengeOptional.get();
        userChallenge.update(dto.getChallengeComment(), dto.getChallengeStar(), dto.getChallengeImage());

        userChallengeRepository.save(userChallenge);
        return true;
    }
    // 유저 도전 조회
    public List<UserChallenge> userChallengeList() {
        return userChallengeRepository.findAll();
    }

    // 유저 도전 삭제.
    public Boolean userChallengeDelete(Long userId, Long courseId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Challenge> challengeOptional = challengeRepository.findById(courseId);

        if (!userOptional.isPresent()) {
            throw new NoExistUserException("Please Check User Id");
        }

        if (!challengeOptional.isPresent()) {
            throw new NoExistUserException("Please Check Challenge Id");
        }


        Optional<UserChallenge> userChallengeOptional = userChallengeRepository.findUserChallengeByUserAndChallenge(
                userOptional.get(), challengeOptional.get()
        );

        if (!userChallengeOptional.isPresent()) {
            throw new IllegalArgumentException("해당 유저와 코스에 일치하는 데이터가 없습니다.");
        }

        userChallengeRepository.delete(userChallengeOptional.get());
        return true;
    }

}