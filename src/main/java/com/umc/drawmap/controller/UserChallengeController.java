package com.umc.drawmap.controller;

import com.umc.drawmap.domain.UserChallenge;
import com.umc.drawmap.dto.userChallenge.UserChallengeReqDto;
import com.umc.drawmap.dto.userChallenge.UserChallengeResDto;
import com.umc.drawmap.exception.BaseResponse;
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
    public BaseResponse<UserChallengeResDto.GetUserChallenge> userChallengeAdd(@RequestBody UserChallengeReqDto.UserChallengeAddDto dto) {
        return new BaseResponse<>(userChallengeService.userChallengeAdd(dto));
    }

    @PatchMapping(path = "/challenge/cert/{courseId}")
    public BaseResponse<UserChallengeResDto.GetUserChallenge> userChallengeUpdate(@PathVariable Long courseId,
                                                                                  @RequestBody UserChallengeReqDto.UserChallengeUpdateDto dto) {
        return new BaseResponse<>(userChallengeService.userChallengeUpdate(courseId, dto));

    }
    // 모든 유저들의 도전 리스트 조회. -
    @GetMapping(path = "/challenge/cert/all")
    public BaseResponse<List<UserChallengeResDto.GetUserChallenge>> userChallengeList() {
        return new BaseResponse<>(userChallengeService.userChallengeList());
    }

    // 특정 유저의 도전 리스트 조회
    @GetMapping(path = "/challenge/cert")
    public BaseResponse<List<UserChallengeResDto.GetUserChallenge>> aUserChallengeList() {

        return new BaseResponse<>(userChallengeService.aUserChallengeList());
    }

    @DeleteMapping(path = "/challenge/cert/{courseId}")
    public BaseResponse<String> userChallengeDelete(@PathVariable Long courseId) {
        userChallengeService.userChallengeDelete(courseId);
        return new BaseResponse<>("삭제 완료.");
    }
}
