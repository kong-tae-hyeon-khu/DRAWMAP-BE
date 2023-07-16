package com.umc.drawmap.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.umc.drawmap.domain.Challenge;
import com.umc.drawmap.dto.challenge.ChallengeConverter;
import com.umc.drawmap.dto.challenge.ChallengeReqDto;
import com.umc.drawmap.dto.challenge.ChallengeResDto;
import com.umc.drawmap.exception.BaseResponse;
import com.umc.drawmap.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ChallengeResDto.ChallengeDto> getChallenge(@PathVariable(name = "courseId")Long courseId){
        Challenge challenge = challengeService.findById(courseId);
        return ResponseEntity.ok(ChallengeConverter.toChallengeDto(challenge));
    }

    // 도전코스 전체 리스트 조회 3개씩
    @GetMapping("/courses")
    public ResponseEntity<ChallengeResDto.ChallengeListDto> getChallengeList(@RequestParam("page")int page){
        Page<Challenge> challengeList = challengeService.findAll(page, 3);
        return ResponseEntity.ok(ChallengeConverter.toChallengeListDto(challengeList));
    }

    // 도전코스 본인 리스트 조회 (참여한 이달의 도전코스) 6개씩
    @GetMapping("/{userId}/courses")
    public ResponseEntity<ChallengeResDto.MyChallengeListDto> getChallengeMyList(@PathVariable(name = "userId")Long userId, @RequestParam("page")int page){
        List<Challenge> challengeMyList = challengeService.findAllByUser(userId, page, 6);
        return ResponseEntity.ok(ChallengeConverter.toChallengeMyListDto(challengeMyList));
    }


    @PostMapping("/course")
    public BaseResponse<String> createChallenge(@RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                @ModelAttribute(value= "request") ChallengeReqDto.CreateChallengeDto request
                                                ) throws IOException{
        Challenge challenge = challengeService.create(files, request);
        return new BaseResponse<>("새로운 도전코스 등록 완료");
    }

    @PatchMapping("/course/{courseId}")
    public BaseResponse<String> updateChallenge(@PathVariable(name = "courseId")Long courseId,
                                                @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                @ModelAttribute ChallengeReqDto.UpdateChallengeDto request) throws IOException{
        Challenge challenge = challengeService.update(courseId,files, request);
        return new BaseResponse<>("도전코스 수정 완료");
    }

    @DeleteMapping("/course/{courseId}")
    public BaseResponse<String> deleteChallenge(@PathVariable(name = "courseId")Long courseId){
        challengeService.delete(courseId);
        return new BaseResponse<>("도전코스 삭제 완료");
    }
}
