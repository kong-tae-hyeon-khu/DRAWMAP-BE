package com.umc.drawmap.service;

import com.umc.drawmap.domain.*;
import com.umc.drawmap.dto.SpotImageResDto;
import com.umc.drawmap.dto.challenge.ChallengeReqDto;
import com.umc.drawmap.dto.challenge.ChallengeResDto;
import com.umc.drawmap.exception.ForbiddenException;
import com.umc.drawmap.exception.NotFoundException;
import com.umc.drawmap.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final UserRepository userRepository;
    private final SpotImageRepository spotImageRepository;
    private final S3FileService s3FileService;
    private final ScrapRepository scrapRepository;


    @Transactional
    public Challenge create(List<MultipartFile> files, ChallengeReqDto.CreateChallengeDto request) throws IOException{

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new NotFoundException("유저가 존재하지 않습니다."));

        if(user.getRole() != Role.ROLE_Admin){
            throw new ForbiddenException("관리자만 접근 가능합니다.");
        }

        Challenge challenge = Challenge.builder()
                .challengeCourseTitle(request.getChallengeCourseTitle())
                .sido(request.getSido())
                .sgg(request.getSgg())
                .challengeCourseContent(request.getChallengeCourseContent())
                .challengeCourseDifficulty(request.getChallengeCourseDifficulty())
                .challengeImage(s3FileService.upload(files)) // S3 로 수정.
                .build();

        return challengeRepository.save(challenge);
    }
    @Transactional
    public Challenge update(Long challengeId,List<MultipartFile> files, ChallengeReqDto.UpdateChallengeDto request) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new NotFoundException("유저가 존재하지 않습니다."));

        if(user.getRole() != Role.ROLE_Admin) {
            throw new ForbiddenException("관리자만 접근 가능합니다.");
        }

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new NotFoundException("도전코스를 찾을 수 없습니다."));
        challenge.update(request.getChallengeCourseTitle(), request.getSido(), request.getSgg(), request.getChallengeCourseDifficulty(), request.getChallengeCourseContent(), s3FileService.upload(files));
        return challenge;
    }

    @Transactional
    public void delete(Long challengeId){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new NotFoundException("유저가 존재하지 않습니다."));

        if(user.getRole() != Role.ROLE_Admin) {
            throw new ForbiddenException("관리자만 접근 가능합니다.");
        }

        challengeRepository.deleteById(challengeId);
    }

    public ChallengeResDto.ChallengeDto findById(Long challengeId){

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(()-> new NotFoundException("도전코스를 찾을 수 없습니다."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new NotFoundException("유저가 존재하지 않습니다."));

        Boolean isScraped = scrapRepository.existsScrapByUserAndChallenge(user, challenge);

        return ChallengeResDto.ChallengeDto.builder()
                .title(challenge.getChallengeCourseTitle())
                .challengeId(challenge.getId())
                .content(challenge.getChallengeCourseContent())
                .sido(challenge.getSido())
                .sgg(challenge.getSgg())
                .image(challenge.getChallengeImage())
                .isScraped(isScraped)
                .createdDate(challenge.getCreatedAt())
                .difficulty(challenge.getChallengeCourseDifficulty())
                .scrapCount(challenge.getScrapCount())
                .build();
    }

    public List<ChallengeResDto.MyChallengeDto> findAllByUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new NotFoundException("유저가 존재하지 않습니다."));

        List<UserChallenge> userChallengeList = userChallengeRepository.findAllByUser(user);
        List<Challenge> list = new ArrayList<>();
        for (UserChallenge userchallenge: userChallengeList){
            Challenge challenge = challengeRepository.findByUserChallenge(userchallenge);
            list.add(challenge);
        }
        List<ChallengeResDto.MyChallengeDto> resultList = new ArrayList<>();
        for (Challenge c: list){
            Boolean isScraped = scrapRepository.existsScrapByUserAndChallenge(user, c);
            ChallengeResDto.MyChallengeDto result = ChallengeResDto.MyChallengeDto.builder()
                    .challengeId(c.getId())
                    .sgg(c.getSgg())
                    .sido(c.getSido())
                    .image(c.getChallengeImage())
                    .createdDate(c.getCreatedAt())
                    .isScraped(isScraped)
                    .build();
            resultList.add(result);
        }
        return resultList;
    }

    public List<ChallengeResDto.ChallengeDto> findAll(){

        List<Challenge> challengeList = challengeRepository.findAll();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new NotFoundException("유저가 존재하지 않습니다."));

        List<ChallengeResDto.ChallengeDto> list = new ArrayList<>();
        for(Challenge challenge: challengeList){
            Boolean isScraped = scrapRepository.existsScrapByUserAndChallenge(user, challenge);
            ChallengeResDto.ChallengeDto result = ChallengeResDto.ChallengeDto.builder()
                    .challengeId(challenge.getId())
                    .title(challenge.getChallengeCourseTitle())
                    .content(challenge.getChallengeCourseContent())
                    .sgg(challenge.getSgg())
                    .sido(challenge.getSido())
                    .path(challenge.getChallengeCoursePath())
                    .isScraped(isScraped)
                    .createdDate(challenge.getCreatedAt())
                    .difficulty(challenge.getChallengeCourseDifficulty())
                    .image(challenge.getChallengeImage())
                    .build();
            list.add(result);
        }
        return list;
    }

    // 도전코스
    public List<ChallengeResDto.ChallengeSortDto> getList(String sort){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new NotFoundException("유저가 존재하지 않습니다."));

        List<Challenge> challengeList;
        if(Objects.equals(sort, "scrapcount")){
            challengeList = challengeRepository.findAllByOrderByScrapCountDesc();
        }else{
            challengeList = challengeRepository.findAllByOrderByCreatedAtDesc();
        }
        List<ChallengeResDto.ChallengeSortDto> list = new ArrayList<>();
        for(Challenge c: challengeList){
            Boolean isScraped = scrapRepository.existsScrapByUserAndChallenge(user, c);
            ChallengeResDto.ChallengeSortDto result = ChallengeResDto.ChallengeSortDto.builder()
                    .challengeId(c.getId())
                    .title(c.getChallengeCourseTitle())
                    .content(c.getChallengeCourseContent())
                    .sgg(c.getSgg())
                    .sido(c.getSido())
                    .isScraped(isScraped)
                    .createdDate(c.getCreatedAt())
                    .image(c.getChallengeImage())
                    .difficulty(c.getChallengeCourseDifficulty())
                    .build();
            list.add(result);
        }
        return list;
    }

    public List<ChallengeResDto.ChallengeSortDto> getListByArea(String sido, String sgg){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new NotFoundException("유저가 존재하지 않습니다."));

        List<Challenge> list = challengeRepository.findAllBySidoOrSgg(sido, sgg);
        List<ChallengeResDto.ChallengeSortDto> resultList = new ArrayList<>();
        for(Challenge c: list){
            Boolean isScraped = scrapRepository.existsScrapByUserAndChallenge(user, c);
            ChallengeResDto.ChallengeSortDto result = ChallengeResDto.ChallengeSortDto.builder()
                    .challengeId(c.getId())
                    .title(c.getChallengeCourseTitle())
                    .content(c.getChallengeCourseContent())
                    .sgg(c.getSgg())
                    .sido(c.getSido())
                    .isScraped(isScraped)
                    .createdDate(c.getCreatedAt())
                    .image(c.getChallengeImage())
                    .difficulty(c.getChallengeCourseDifficulty())
                    .build();
            resultList.add(result);
        }
        return resultList;
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
