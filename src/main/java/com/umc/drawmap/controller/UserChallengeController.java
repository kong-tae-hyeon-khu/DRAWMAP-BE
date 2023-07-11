package com.umc.drawmap.controller;

import com.umc.drawmap.dto.userChallenge.UserChallengeReqDto;
import com.umc.drawmap.service.UserChallengeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserChallengeController {
    private final UserChallengeService userChallengeService;

    public UserChallengeController(UserChallengeService userChallengeService) {
        this.userChallengeService = userChallengeService;
    }

    @PostMapping(path = "/challenge/cert")
    public String userChallengeAdd(@RequestBody UserChallengeReqDto.UserChallengeAddDto dto) {
        System.out.println(dto.getUserId());
        userChallengeService.userChallengeAdd(dto);
        return "인증 등록 완료.";
    }
}
