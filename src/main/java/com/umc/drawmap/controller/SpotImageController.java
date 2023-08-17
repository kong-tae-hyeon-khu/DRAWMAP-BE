package com.umc.drawmap.controller;

import com.umc.drawmap.domain.SpotImage;
import com.umc.drawmap.dto.SpotImageReqDto;
import com.umc.drawmap.dto.SpotImageResDto;
import com.umc.drawmap.exception.BaseResponse;
import com.umc.drawmap.service.SpotImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/challenge")
public class SpotImageController {

    private final SpotImageService spotImageService;

    @PostMapping(path = "/spot", consumes = {"multipart/form-data"})
    public BaseResponse<SpotImageResDto.SpotImageIdDto> createSpotImage(@RequestPart(value = "file", required = false) List<MultipartFile> files,
                                                @ModelAttribute(value = "request")SpotImageReqDto.CreateSpotImageDto request) throws IOException{

        SpotImage spotImage = spotImageService.create(files, request);
        return new BaseResponse<>(SpotImageResDto.SpotImageIdDto.builder().spotImageId(spotImage.getId()).build());

    }

    @PatchMapping(path = "/spot/{courseId}/{spotId}", consumes = {"multipart/form-data"})
    public BaseResponse<SpotImageResDto.SpotImageIdDto> updateSpotImage(@PathVariable(name = "courseId") Long courseId, @PathVariable(name = "spotId")Long spotId,
                                                @RequestPart(value = "file", required = false)List<MultipartFile> files,
                                                @ModelAttribute SpotImageReqDto.UpdateSpotImageDto request) throws IOException{
        SpotImage spotImage = spotImageService.update(courseId, spotId, files, request);
        return new BaseResponse<>(SpotImageResDto.SpotImageIdDto.builder().spotImageId(spotImage.getId()).build());
    }

    // 관광지 전체 조회
    @GetMapping("/spot/{courseId}")
    public BaseResponse<List<SpotImageResDto.SpotImageDto>> getSpot(@PathVariable(name = "courseId")Long courseId){
        return new BaseResponse<>(spotImageService.findAllByCourse(courseId));
    }


    @DeleteMapping("/spot/{courseId}/{spotId}")
    public BaseResponse<String> deleteSpotImage(@PathVariable(value = "courseId")Long courseId, @PathVariable(value = "spotId")Long spotId){
        spotImageService.delete(courseId, spotId);
        return new BaseResponse<>("관광지 삭제 완료");

    }

}
