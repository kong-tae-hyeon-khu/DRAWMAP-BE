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
    private final ScrapService scrapService;
    public ScrapController(ScrapService scrapService) {
        this.scrapService = scrapService;
    }


    @PostMapping("/scrap") // 혜진님 여기 principal 받는 부분을 제가 잠시 지웠어요!!
    public BaseResponse<ScrapResDto.ScrapDto> addScrap(@RequestBody ScrapReqDto.ScrapAddDto dto) {
        if (dto.getChallenge_id() == null) {
            return new BaseResponse<>(scrapService.addUserCourseScrap(dto));
        }
        return new BaseResponse<>(scrapService.addChallengeScrap(dto));
    }


    @GetMapping("/scrap")
    public BaseResponse<ScrapResDto.ScrapListDto> getScrap() {
        return new BaseResponse<>(scrapService.getMyScrap());
    }
    @DeleteMapping("/scrap")  // 쿼리 스트링으로 유저코스 또는 도전코스를 입력받아야한다.
    public BaseResponse<ScrapResDto.ScrapDto> deleteCourseScrap(Long courseId, Long challengeId) {
        return new BaseResponse<>(scrapService.deleteMyScrap(courseId, challengeId));
    }
}
