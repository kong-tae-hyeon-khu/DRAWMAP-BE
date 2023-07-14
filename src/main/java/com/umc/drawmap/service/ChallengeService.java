package com.umc.drawmap.service;

import com.umc.drawmap.domain.Challenge;
import com.umc.drawmap.dto.challenge.ChallengeReqDto;
import com.umc.drawmap.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

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
        Challenge challenge = challengeRepository.findById(challengeId).get();
        challenge.update(request.getChallengeCourseTitle(), request.getChallengeCourseArea(), request.getChallengeCourseDifficulty(), request.getChallengeCourseContent(), FileService.fileUpload(files));
        return challenge;
    }

    public void delete(Long challengeId){
        challengeRepository.deleteById(challengeId);
    }

}
