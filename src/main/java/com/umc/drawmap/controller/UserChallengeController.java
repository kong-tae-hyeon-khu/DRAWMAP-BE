package com.umc.drawmap.controller;

import com.umc.drawmap.dto.userChallenge.UserChallengeReqDto;
import com.umc.drawmap.service.UserChallengeService;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserChallengeController {
    private final UserChallengeService userChallengeService;

    public UserChallengeController(UserChallengeService userChallengeService) {
        this.userChallengeService = userChallengeService;
    }

    @PostMapping(path = "/challenge/cert")
    public String userChallengeAdd(@RequestBody UserChallengeReqDto.UserChallengeAddDto dto) {
        userChallengeService.userChallengeAdd(dto);
        return "인증 등록 완료.";
    }

    @PatchMapping(path = "/challenge/cert/{userId}/{courseId}")
    public String userChallengeUpdate(@PathVariable Long userId, @PathVariable Long courseId,
                                      @RequestBody UserChallengeReqDto.UserChallengeUpdateDto dto) {
        userChallengeService.userChallengeUpdate(userId, courseId, dto);
        return "수정 완료.";
    }
}
