package com.umc.drawmap.controller;


import com.umc.drawmap.dto.user.UserReqDto;
import com.umc.drawmap.dto.user.UserResDto;
import com.umc.drawmap.exception.BaseResponse;
import com.umc.drawmap.service.UserService;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    // 유저 기본 정보 조회.
    @GetMapping("/user/{userId}")
    public BaseResponse<UserResDto.UserDto> getUser(@PathVariable Long userId) {
        return new BaseResponse<>(userService.getUserInfo(userId));
    }

    // 닉네임 중복 확인
    @GetMapping("/users/{nickName}")
    public BaseResponse<UserResDto.UserNameDto> checkName(@PathVariable String nickName) {
        return new BaseResponse<>(userService.checkUserName(nickName));
    }

    // 이메일 중복 확인 (URI 수정하는게 좋을듯..!)
    @GetMapping("/email/{email}")
    public BaseResponse<UserResDto.UserEmailDto> checkEmail(@PathVariable String email) {
        System.out.println(email);
        return new BaseResponse<>(userService.checkUserEmail(email));
    }

    // 유저 정보 수정
    @PatchMapping("/user/{userId}")
    public BaseResponse<UserResDto.UserDto> updateUser(@PathVariable Long userId, @RequestBody UserReqDto.updateDto dto) {
        return new BaseResponse<>(userService.updateUser(userId, dto));
    }

}
