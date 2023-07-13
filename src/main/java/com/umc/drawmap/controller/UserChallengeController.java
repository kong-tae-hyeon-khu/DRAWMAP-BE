package com.umc.drawmap.controller;

import com.umc.drawmap.domain.UserChallenge;
import com.umc.drawmap.dto.userChallenge.UserChallengeReqDto;
import com.umc.drawmap.service.UserChallengeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    // 모든 유저들의 도전 리스트 조회.
    @GetMapping(path = "/challenge/cert")
    public List<UserChallenge> userChallengeList() {
        return userChallengeService.userChallengeList();
    }

    @DeleteMapping(path = "/challenge/cert/{userId}/{courseId}")
    public String userChallengeDelete(@PathVariable Long userId, @PathVariable Long courseId) {
        userChallengeService.userChallengeDelete(userId, courseId);
        return "삭제 완료.";
    }
}
