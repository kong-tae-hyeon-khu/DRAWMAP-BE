package com.umc.drawmap.controller;

import com.umc.drawmap.domain.Challenge;
import com.umc.drawmap.domain.Role;
import com.umc.drawmap.domain.User;
import com.umc.drawmap.dto.challenge.ChallengeReqDto;
import com.umc.drawmap.dto.challenge.ChallengeResDto;
import com.umc.drawmap.exception.BaseResponse;
import com.umc.drawmap.exception.BaseResponseStatus;
import com.umc.drawmap.exception.ForbiddenException;
import com.umc.drawmap.exception.NotFoundException;
import com.umc.drawmap.service.ChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.umc.drawmap.exception.BaseResponseStatus.ROLE_EXCEPTION;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/challenge")
public class ChallengeController {

    private final ChallengeService challengeService;

    // 도전코스 상세페이지 조회
    @GetMapping("/{courseId}")
    public BaseResponse<ChallengeResDto.ChallengeDto> getChallenge(@PathVariable(name = "courseId")Long courseId){
        return new BaseResponse<>(challengeService.findById(courseId));
    }

    // 도전코스 전체 리스트 조회
    @GetMapping("/courses")
    public BaseResponse<List<ChallengeResDto.ChallengeDto>> getChallengeList(){
        return new BaseResponse<>(challengeService.findAll());
    }

    // 도전코스 본인 리스트 조회 (참여한 이달의 도전코스)
    @GetMapping("/courses/mylist")
    public BaseResponse<List<ChallengeResDto.MyChallengeDto>> getChallengeMyList(){
        return new BaseResponse<>(challengeService.findAllByUser());
    }

    // 도전코스 정렬 (최신순, 인기순)
    @GetMapping("/list")
    @Operation(description = "Default 최신순 정렬, 인기순 정렬을 원할 경우 sort에 scrapcount를 입력")
    public BaseResponse<List<ChallengeResDto.ChallengeSortDto>> getChallengeListByCreatedAt(@RequestParam(name = "sort", defaultValue = "createdAt", required = false) String sort){
        return new BaseResponse<>(challengeService.getList(sort));
    }

    // 도전코스 정렬 (지역별)
    @GetMapping("/arealist")
    public BaseResponse<List<ChallengeResDto.ChallengeSortDto>> getChallengeListByArea(@RequestParam(name = "sido") String sido, @RequestParam(name = "sgg")String sgg){
        return new BaseResponse<>(challengeService.getListByArea(sido, sgg));
    }

    // 관광지 추천 (코스사진이랑 관광지사진만)
    @GetMapping("/spot/image/{courseId}")
    public BaseResponse<ChallengeResDto.ChallengeDetailDto> getSpotRecommend(@PathVariable(name = "courseId")Long courseId){
        return new BaseResponse<>(challengeService.getSpotRecommend(courseId));
    }


    @PostMapping(path = "/course", consumes = {"multipart/form-data"})
    public BaseResponse<ChallengeResDto.ChallengeIdDto> createChallenge(@RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                @ModelAttribute(value= "request") ChallengeReqDto.CreateChallengeDto request) throws IOException{

        try{
            Challenge challenge = challengeService.create(files, request);
            return new BaseResponse<>(ChallengeResDto.ChallengeIdDto.builder().challengeId(challenge.getId()).build());
        }
        catch (Exception e){
            return new BaseResponse<>(ROLE_EXCEPTION);
        }
    }

    @PatchMapping(path = "/course/{courseId}", consumes = {"multipart/form-data"})
    public BaseResponse<ChallengeResDto.ChallengeIdDto> updateChallenge(@PathVariable(name = "courseId")Long courseId,
                                                @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                @ModelAttribute ChallengeReqDto.UpdateChallengeDto request) throws IOException{
        try{
            Challenge challenge = challengeService.update(courseId,files, request);
            return new BaseResponse<>(ChallengeResDto.ChallengeIdDto.builder().challengeId(challenge.getId()).build());
        }
        catch (Exception e){
            return new BaseResponse<>(ROLE_EXCEPTION);
        }

    }

    @DeleteMapping("/course/{courseId}")
    public BaseResponse<String> deleteChallenge(@PathVariable(name = "courseId")Long courseId){
        try{
            challengeService.delete(courseId);
            return new BaseResponse<>("도전코스 삭제 완료");
        }
        catch (Exception e){
            return new BaseResponse<>(ROLE_EXCEPTION);
        }
    }


}
