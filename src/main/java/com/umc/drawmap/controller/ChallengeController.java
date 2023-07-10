package com.umc.drawmap.controller;

import com.umc.drawmap.dto.ChallengeReqDto;
import com.umc.drawmap.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/challenge")
public class ChallengeController {

    private final ChallengeService challengeService;

    @PostMapping("/course")
    public ResponseEntity<String> createChallenge(@RequestBody ChallengeReqDto.CreateChallengeDto request)

    @PatchMapping("/course/{courseId}")
}
