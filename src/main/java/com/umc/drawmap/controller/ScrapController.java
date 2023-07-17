package com.umc.drawmap.controller;

import com.umc.drawmap.domain.Scrap;
import com.umc.drawmap.dto.scrap.ScrapReqDto;
import com.umc.drawmap.dto.scrap.ScrapResDto;
import com.umc.drawmap.service.ScrapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ScrapController {
    private ScrapService scrapService;
    public ScrapController(ScrapService scrapService) {
        this.scrapService = scrapService;
    }
    @PostMapping("/scrap")
    public String addScrap(@RequestBody ScrapReqDto.ScrapAddDto dto) {
        if (dto.getChallenge_id() == null) {
            return scrapService.addUserCourseScrap(dto);
        }

        return scrapService.addChallengeScrap(dto);
    }
    @GetMapping("/scrap/{userId}")
    public ResponseEntity<List<ScrapResDto.ScrapDto>>  getScrap(@PathVariable Long userId) {
        return scrapService.getMyScrap(userId);
    }
}
