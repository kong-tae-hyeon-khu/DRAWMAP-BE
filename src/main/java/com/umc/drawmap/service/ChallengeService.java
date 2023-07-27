package com.umc.drawmap.service;

import com.umc.drawmap.domain.Challenge;
import com.umc.drawmap.domain.SpotImage;
import com.umc.drawmap.domain.User;
import com.umc.drawmap.domain.UserChallenge;
import com.umc.drawmap.dto.SpotImageResDto;
import com.umc.drawmap.dto.challenge.ChallengeReqDto;
import com.umc.drawmap.dto.challenge.ChallengeResDto;
import com.umc.drawmap.exception.NotFoundException;
import com.umc.drawmap.repository.ChallengeRepository;
import com.umc.drawmap.repository.SpotImageRepository;
import com.umc.drawmap.repository.UserChallengeRepository;
import com.umc.drawmap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final SpotImageRepository spotImageRepository;

    @Transactional
    public Challenge create(List<MultipartFile> files, ChallengeReqDto.CreateChallengeDto request) throws IOException{

        Challenge challenge = Challenge.builder()
                .challengeCourseTitle(request.getChallengeCourseTitle())
                .sido(request.getSido())
                .sgg(request.getSgg())
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
        challenge.update(request.getChallengeCourseTitle(), request.getSido(), request.getSgg(), request.getChallengeCourseDifficulty(), request.getChallengeCourseContent(), FileService.fileUpload(files));
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

    public Page<Challenge> getPageByArea(int page, String sido, String sgg){
        PageRequest pageRequest = PageRequest.of(page, 6);
        return challengeRepository.findAllBySidoOrSgg(sido, sgg, pageRequest);
    }

    public ChallengeResDto.ChallengeDetailDto getSpotRecommend(Long courseId){
        Challenge challenge = challengeRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("도전코스를 찾을 수 없습니다."));
        List<SpotImage> spotImageList = spotImageRepository.findAllByChallenge(challenge);
        List<SpotImageResDto.SpotImageBriefDto> list = new ArrayList<>();
        for(SpotImage i: spotImageList){
            SpotImageResDto.SpotImageBriefDto dto = SpotImageResDto.SpotImageBriefDto.builder()
                    .spotImageId(i.getId())
                    .title(i.getSpotTitle())
                    .image(i.getSpotImage())
                    .build();
            list.add(dto);
        }
        return ChallengeResDto.ChallengeDetailDto.builder()
                .challengeId(challenge.getId())
                .title(challenge.getChallengeCourseTitle())
                .sido(challenge.getSido())
                .sgg(challenge.getSgg())
                .createdDate(challenge.getCreatedAt())
                .spotImage(list)
                .build();
    }

}
