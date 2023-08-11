package com.umc.drawmap.controller;

import com.umc.drawmap.domain.Challenge;
import com.umc.drawmap.dto.challenge.ChallengeReqDto;
import com.umc.drawmap.dto.challenge.ChallengeResDto;
import com.umc.drawmap.exception.BaseResponse;
import com.umc.drawmap.repository.UserRepository;
import com.umc.drawmap.service.ChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    // 도전코스 전체 리스트 조회 3개씩
    @GetMapping("/courses")
    public BaseResponse<List<ChallengeResDto.ChallengeDto>> getChallengeList(@RequestParam(name = "page")int page){
        return new BaseResponse<>(challengeService.findAll(page));
    }

    // 도전코스 본인 리스트 조회 (참여한 이달의 도전코스) 6개씩
    @GetMapping("/courses/mylist")
    public BaseResponse<List<ChallengeResDto.MyChallengeDto>> getChallengeMyList(@RequestParam(name = "page")int page){
        return new BaseResponse<>(challengeService.findAllByUser(page));
    }

    // 도전코스 정렬 (최신순, 인기순) 도전코스 6개씩
    // 페이지 0부터 시작 -> ex) 6개씩 있는 2페이지 조회 PageRequest.of((page)1,(size)5)
    @GetMapping("/list")
    @Operation(description = "최신순 정렬을 원할 경우 page만 입력, 인기순 정렬을 원할 경우 page와 sort에 likecount를 입력")
    public BaseResponse<List<ChallengeResDto.ChallengeSortDto>> getChallengeListByCreatedAt(@RequestParam(name = "page")int page,
                                                                                            @RequestParam(name = "sort", defaultValue = "createdAt", required = false) String sort){
        return new BaseResponse<>(challengeService.getPage(page-1, sort));
    }

    // 도전코스 정렬 (지역별)
    @GetMapping("/arealist")
    public BaseResponse<List<ChallengeResDto.ChallengeSortDto>> getChallengeListByArea(@RequestParam(name = "page")int page, @RequestParam(name = "sido") String sido, @RequestParam(name = "sgg")String sgg){
        return new BaseResponse<>(challengeService.getPageByArea(page-1, sido, sgg));
    }

    // 관광지 추천 (코스사진이랑 관광지사진만)
    @GetMapping("/spot/image/{courseId}")
    public BaseResponse<ChallengeResDto.ChallengeDetailDto> getSpotRecommend(@PathVariable(name = "courseId")Long courseId){
        return new BaseResponse<>(challengeService.getSpotRecommend(courseId));
    }


    @PostMapping("/course")
    public BaseResponse<String> createChallenge(@RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                @ModelAttribute(value= "request") ChallengeReqDto.CreateChallengeDto request
                                                ) throws IOException{
        challengeService.create(files, request);
        return new BaseResponse<>("새로운 도전코스 등록 완료");
    }

    @PatchMapping("/course/{courseId}")
    public BaseResponse<String> updateChallenge(@PathVariable(name = "courseId")Long courseId,
                                                @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                @ModelAttribute ChallengeReqDto.UpdateChallengeDto request) throws IOException{
        challengeService.update(courseId,files, request);
        return new BaseResponse<>("도전코스 수정 완료");
    }

    @DeleteMapping("/course/{courseId}")
    public BaseResponse<String> deleteChallenge(@PathVariable(name = "courseId")Long courseId){
        challengeService.delete(courseId);
        return new BaseResponse<>("도전코스 삭제 완료");
    }


}
