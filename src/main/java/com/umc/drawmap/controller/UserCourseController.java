package com.umc.drawmap.controller;

import com.umc.drawmap.dto.UserCourseReqDto;
import com.umc.drawmap.exception.BaseResponse;
import com.umc.drawmap.service.UserCourseService;
import com.umc.drawmap.domain.UserCourse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserCourseController {
    private final UserCourseService userCourseService;

    // 등록
    @PostMapping("/usercourse")
    public BaseResponse<String> createUserCourse(@RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                @ModelAttribute(value= "request") UserCourseReqDto.CreateUserCourseDto request
    ) throws IOException {
        UserCourse userCourse = userCourseService.create(files, request);
        return new BaseResponse<>("유저코스 등록 완료");
    }

    // 수정
    @PatchMapping("/usercourse/{ucourseId}")
    public BaseResponse<String> updateUserCourse(@PathVariable(name = "ucourseId")Long ucourseId,
                                                @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                @ModelAttribute UserCourseReqDto.UpdateUserCourseDto request
    ) throws IOException{
        UserCourse userCourse = userCourseService.update(ucourseId, files, request);
        return new BaseResponse<>("유저코스 수정 완료");
    }

    // 전체 리스트 조회
    @GetMapping("/usercourse")
    public List<UserCourse> userCourseList() {
        return userCourseService.userCourseList();
    }

    // 삭제
    @DeleteMapping("usercourse/{ucourseId}")
    public BaseResponse<String> deleteUserCourse(@PathVariable(name = "ucourseId")Long ucourseId){
        userCourseService.delete(ucourseId);
        return new BaseResponse<>("유저코스 삭제 완료");
    }
}
