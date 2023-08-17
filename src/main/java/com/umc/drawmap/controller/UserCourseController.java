package com.umc.drawmap.controller;

import com.umc.drawmap.dto.UserCourseReqDto;
import com.umc.drawmap.dto.UserCourseResDto;
import com.umc.drawmap.dto.user.UserResDto;
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
    @PostMapping(path = "/usercourse", consumes = {"multipart/form-data"})
    public BaseResponse<UserCourseResDto.UserCourseIdDto> createUserCourse(@RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                 @ModelAttribute(value= "request") UserCourseReqDto.CreateUserCourseDto request) throws IOException {
        UserCourse userCourse = userCourseService.create(files, request);
        return new BaseResponse<>(UserCourseResDto.UserCourseIdDto.builder().userCourseId(userCourse.getId()).build());
    }

    // 수정
    @PatchMapping(path = "/usercourse/{ucourseId}", consumes = {"multipart/form-data"})
    public BaseResponse<UserCourseResDto.UserCourseIdDto> updateUserCourse(@PathVariable(name = "ucourseId")Long ucourseId,
                                                @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                @ModelAttribute UserCourseReqDto.UpdateUserCourseDto request
    ) throws IOException{
        UserCourse userCourse = userCourseService.update(ucourseId, files, request);
        return new BaseResponse<>(UserCourseResDto.UserCourseIdDto.builder().userCourseId(userCourse.getId()).build());
    }

    // 전체 리스트 조회
    @GetMapping("/usercourse")
    public List<UserCourseResDto.UserCourseDto> userCourseList() {
        return userCourseService.userCourseList();
    }

    // 본인 리스트 조회
    @GetMapping("usercourse/mylist")
    public BaseResponse<List<UserCourseResDto.MyUserCourseDto>> getUserCourseMyList(){
        return new BaseResponse<>(userCourseService.findAllByUser());
    }

    // 상세 페이지 조회
    @GetMapping("usercourse/{ucourseId}")
    public BaseResponse<UserCourseResDto.UserCourseDto> getUserCourse(@PathVariable(name = "ucourseId") Long ucourseId){
        return new BaseResponse<>(userCourseService.findById(ucourseId));
    }

    // 삭제
    @DeleteMapping("/usercourse/{ucourseId}")
    public BaseResponse<String> deleteUserCourse(@PathVariable(name = "ucourseId")Long ucourseId){
        userCourseService.delete(ucourseId);
        return new BaseResponse<>("유저코스 삭제 완료");
    }

    // 유저코스 정렬 (최신순, 인기순)
    @GetMapping("usercourse/list")
    public BaseResponse<List<UserCourseResDto.UserCourseSortDto>> getChallengeListByCreatedAt(@RequestParam(name = "sort", defaultValue = "createdAt", required = false) String sort){
        return new BaseResponse<>(userCourseService.getList(sort));
    }

    // 유저코스 정렬 (지역별)
    @GetMapping("usercourse/arealist")
    public BaseResponse<List<UserCourseResDto.UserCourseSortDto>> getChallengeListByArea(@RequestParam(name = "sido") String sido, @RequestParam(name = "sgg") String sgg){
        return new BaseResponse<>(userCourseService.getListByArea(sido, sgg));
    }

    // Top3 유저 정렬 (유저개발코스의 찜 개수)
    @GetMapping("/usercourse/user")
    public BaseResponse<List<UserResDto.UserDto>> getTop3User(){
        return new BaseResponse<>(userCourseService.getTop3User());
    }

}
