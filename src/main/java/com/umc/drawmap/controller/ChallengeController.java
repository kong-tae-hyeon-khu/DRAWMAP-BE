package com.umc.drawmap.controller;

import com.umc.drawmap.domain.Challenge;
import com.umc.drawmap.dto.ChallengeReqDto;
import com.umc.drawmap.exception.BaseResponse;
import com.umc.drawmap.service.ChallengeService;
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
}
