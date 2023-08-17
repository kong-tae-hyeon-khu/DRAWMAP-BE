package com.umc.drawmap.service;
import com.umc.drawmap.domain.Challenge;
import com.umc.drawmap.domain.User;
import com.umc.drawmap.domain.UserChallenge;
import com.umc.drawmap.dto.userChallenge.UserChallengeReqDto;
import com.umc.drawmap.dto.userChallenge.UserChallengeResDto;
import com.umc.drawmap.exception.userChallenge.DuplicateUserChallengeException;
import com.umc.drawmap.exception.userChallenge.NoExistUserException;
import com.umc.drawmap.repository.ChallengeRepository;
import com.umc.drawmap.repository.UserChallengeRepository;
import com.umc.drawmap.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    // 유저 도전 등록(인증)
    public UserChallengeResDto.GetUserChallenge userChallengeAdd(UserChallengeReqDto.UserChallengeAddDto dto) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Long userId = Long.parseLong(username);

        // 파라미터 검증

        if (dto.getChallengeId() == null) {
            throw new IllegalArgumentException("Challenge ID cannot be null");
        }

        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Challenge> challengeOptional = challengeRepository.findById(dto.getChallengeId());

        if (!userOptional.isPresent()) {
            throw new NoExistUserException("Please Check User Id");
        }

        if (!challengeOptional.isPresent()) {
            throw new NoExistUserException("Please Check Challenge Id");
        }



        User user = userOptional.get();
        Challenge challenge = challengeOptional.get();

        if (userChallengeRepository.findUserChallengeByUserAndChallenge(user, challenge).isPresent()) {
            throw new DuplicateUserChallengeException("이미 도전 인증을 하였습니다.");
        }

        // DB 저장.
        UserChallenge userChallenge = UserChallenge.builder()
                .challengeComment(dto.getChallengeComment())
                .challenge(challenge)
                .user(user)
                .challengeStar(dto.getChallengeStar())
                .challengeImage(dto.getChallengeImage())
                .build();
        userChallengeRepository.save(userChallenge);

        challenge = challenge.updateUserChallenge(userChallenge);
        challengeRepository.save(challenge);
        user = user.updateUserChallenge(userChallenge);
        userRepository.save(user);

        // Response DTO 생성.
        UserChallengeResDto.GetUserChallenge resDto = UserChallengeResDto.GetUserChallenge.builder()
                .userId(user.getId())  // 유저 이름으로 변경가능.
                .challengeId(challenge.getId()) // 도전 이름으로 변경가능.
                .challengeComment(dto.getChallengeComment())
                .challengeStar(dto.getChallengeStar())
                .challengeImage(dto.getChallengeImage())
                .build();

        return resDto;
    }
    // 유저 도전 수정!
    public UserChallengeResDto.GetUserChallenge userChallengeUpdate(Long challengeId ,UserChallengeReqDto.UserChallengeUpdateDto dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Long userId = Long.parseLong(username);

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

        UserChallengeResDto.GetUserChallenge resDto = UserChallengeResDto.GetUserChallenge.builder()
                .userId(userId)
                .challengeId(challengeId)
                .challengeComment(dto.getChallengeComment())
                .challengeImage(dto.getChallengeImage())
                .challengeStar(dto.getChallengeStar())
                .build();

        return resDto;
    }

    // 유저 전체 도전 조회
    public List<UserChallengeResDto.GetUserChallenge> userChallengeList() {

        List<UserChallenge> userChallengeList = userChallengeRepository.findAll();
        List<UserChallengeResDto.GetUserChallenge> getUserChallengeList = new ArrayList<>();
        for (UserChallenge userChallenge : userChallengeList) {
            UserChallengeResDto.GetUserChallenge dto = UserChallengeResDto.GetUserChallenge.builder()
                    .challengeId(userChallenge.getChallenge().getId())
                    .challengeComment(userChallenge.getChallengeComment())
                    .userId(userChallenge.getUser().getId())
                    .challengeImage(userChallenge.getChallengeImage())
                    .challengeStar(userChallenge.getChallengeStar())
                    .build();

            getUserChallengeList.add(dto);
        }
        return getUserChallengeList;
    }

    // 특정 유저 도전 조회
    public List<UserChallengeResDto.GetUserChallenge> aUserChallengeList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Long userId = Long.parseLong(username);

        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new NoExistUserException("해당 유저가 존재하지 않습니다.");
        }

        User user = userOptional.get();

        List<UserChallengeResDto.GetUserChallenge> getUserChallengeArrayList = new ArrayList<>();
        List<UserChallenge> userChallengeList = userChallengeRepository.findAllByUser(user);
        for (UserChallenge userChallenge : userChallengeList) {
            UserChallengeResDto.GetUserChallenge dto = UserChallengeResDto.GetUserChallenge.builder()
                    .challengeStar(userChallenge.getChallengeStar())
                    .challengeImage(userChallenge.getChallengeImage())
                    .userId(userChallenge.getUser().getId())
                    .challengeComment(userChallenge.getChallengeComment())
                    .challengeId(userChallenge.getChallenge().getId())
                    .build();

            getUserChallengeArrayList.add(dto);
        }
        return getUserChallengeArrayList;
    }
    // 유저 도전 삭제.
    public Boolean userChallengeDelete(Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Long userId = Long.parseLong(username);

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