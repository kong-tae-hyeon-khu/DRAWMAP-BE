package com.umc.drawmap.service;

import com.umc.drawmap.domain.Challenge;
import com.umc.drawmap.domain.Region;
import com.umc.drawmap.domain.User;
import com.umc.drawmap.domain.UserChallenge;
import com.umc.drawmap.dto.challenge.ChallengeReqDto;
import com.umc.drawmap.exception.NotFoundException;
import com.umc.drawmap.repository.ChallengeRepository;
import com.umc.drawmap.repository.UserChallengeRepository;
import com.umc.drawmap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final UserRepository userRepository;

    @Transactional
    public Challenge create(List<MultipartFile> files, ChallengeReqDto.CreateChallengeDto request) throws IOException{

        Challenge challenge = Challenge.builder()
                .challengeCourseTitle(request.getChallengeCourseTitle())
                .challengeCourseArea(request.getChallengeCourseArea())
                .challengeCourseContent(request.getChallengeCourseContent())
                .challengeCourseDifficulty(request.getChallengeCourseDifficulty())
                .challengeImage(FileService.fileUpload(files))
                .build();

        return challengeRepository.save(challenge);
    }
    @Transactional
    public Challenge update(Long challengeId,List<MultipartFile> files, ChallengeReqDto.UpdateChallengeDto request) throws IOException{
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new NotFoundException("도전코스를 찾을 수 없습니다."));
        challenge.update(request.getChallengeCourseTitle(), request.getChallengeCourseArea(), request.getChallengeCourseDifficulty(), request.getChallengeCourseContent(), FileService.fileUpload(files));
        return challenge;
    }

    public void delete(Long challengeId){
        challengeRepository.deleteById(challengeId);
    }

    public Challenge findById(Long challengeId){
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(()-> new NotFoundException("도전코스를 찾을 수 없습니다."));
        return challenge;
    }

    public List<Challenge> findAllByUser(Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
        List<UserChallenge> userChallengeList = userChallengeRepository.findAllByUser(user);
        List<Challenge> list = new ArrayList<>();
        for (UserChallenge userchallenge: userChallengeList){
            Challenge challenge = challengeRepository.findChallengeByUserChallenge(userchallenge);
            list.add(challenge);
        }
        return list;
    }

    public List<Challenge> findAll(){
        List<Challenge> challengeList = challengeRepository.findAll();
        return challengeList;
    }

    // 도전코스 6개씩
    public Page<Challenge> getPage(int page){
        PageRequest pageRequest = PageRequest.of(page, 6, Sort.by(Sort.Direction.DESC, "createdAt"));
        return challengeRepository.findAll(pageRequest);
    }

    public Page<Challenge> getPageByScrap(int page){
        PageRequest pageRequest = PageRequest.of(page, 6, Sort.by(Sort.Direction.DESC, "scrapCount"));
        return challengeRepository.findAll(pageRequest);
    }

    public Page<Challenge> getPageByArea(int page, Region area){
        PageRequest pageRequest = PageRequest.of(page, 6);
        return challengeRepository.findAllByChallengeCourseArea(area, pageRequest);
    }

}
