package com.umc.drawmap.controller;

import com.umc.drawmap.dto.user.UserResDto;
import com.umc.drawmap.exception.BaseResponse;
import com.umc.drawmap.service.CustomOAuth2UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final CustomOAuth2UserService customOAuth2UserService;
    public UserController(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }
    // 유저 기본 정보 조회.
    @GetMapping("/user/{userId}")
    public BaseResponse<UserResDto.UserDto> getUser(@PathVariable Long userId) {
        return new BaseResponse<>(customOAuth2UserService.getUserInfo(userId));
    }

    // 닉네임 중복 확인
    @GetMapping("/users/{nickName}")
    public BaseResponse<UserResDto.UserNameDto> checkName(@PathVariable String nickName) {
        return new BaseResponse<>(customOAuth2UserService.checkUserName(nickName));
    }

    // 이메일 중복 확인 (URI 수정하는게 좋을듯..!)
    @GetMapping("/email/{email}")
    public BaseResponse<UserResDto.UserEmailDto> checkEmail(@PathVariable String email) {
        System.out.println(email);
        return new BaseResponse<>(customOAuth2UserService.checkUserEmail(email));
    }
}
