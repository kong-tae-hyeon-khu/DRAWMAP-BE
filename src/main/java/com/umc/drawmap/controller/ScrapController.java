package com.umc.drawmap.controller;

import com.umc.drawmap.dto.scrap.ScrapReqDto;
import com.umc.drawmap.dto.scrap.ScrapResDto;
import com.umc.drawmap.exception.BaseResponse;
import com.umc.drawmap.service.ScrapService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
public class ScrapController {
    private ScrapService scrapService;
    public ScrapController(ScrapService scrapService) {
        this.scrapService = scrapService;
    }


    @PostMapping("/scrap")
    public BaseResponse<ScrapResDto.ScrapDto> addScrap(Principal principal, @RequestBody ScrapReqDto.ScrapAddDto dto) {
        if (dto.getChallenge_id() == null) {
            return new BaseResponse<>(scrapService.addUserCourseScrap(dto, principal));
        }
        return new BaseResponse<>(scrapService.addChallengeScrap(dto, principal));
    }


    @GetMapping("/scrap/{userId}")
    public BaseResponse<ScrapResDto.ScrapListDto> getScrap(@PathVariable Long userId) {
        return new BaseResponse<>(scrapService.getMyScrap(userId));
    }
    @DeleteMapping("/scrap/{userId}")  // 쿼리 스트링으로 유저코스 또는 도전코스를 입력받아야한다.
    public BaseResponse<ScrapResDto.ScrapDto> deleteCourseScrap(@PathVariable Long userId, Long courseId, Long challengeId) {
        return new BaseResponse<>(scrapService.deleteMyScrap(userId, courseId, challengeId));
    }
}
