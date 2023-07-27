package com.umc.drawmap.controller;


import com.umc.drawmap.dto.user.UserReqDto;
import com.umc.drawmap.dto.user.UserResDto;
import com.umc.drawmap.exception.BaseResponse;
import com.umc.drawmap.service.CustomOAuth2UserService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


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

    // 유저 정보 수정
    @PatchMapping("/user/{userId}")
    public BaseResponse<UserResDto.UserDto> updateUser(@PathVariable Long userId, @RequestBody UserReqDto.updateDto dto) {
        return new BaseResponse<>(customOAuth2UserService.updateUser(userId, dto));
    }

    // 인가코드 받아오기
    @ResponseBody
    @GetMapping("/callback")
    public BaseResponse<String> kakaoCallback(@RequestParam String code){
        String response = "성공적으로 카카오 로그인 API 코드를 불러왔습니다.";
        System.out.println(code);
        return new BaseResponse<>(response);
    }

    @GetMapping("/oauth2/kakao")
    public String getAccessToken(@RequestParam("code") String code) {
        System.out.println("code = " + code);

        // 1. header 생성
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

        // 2. body 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code"); //고정값
        params.add("client_id", "client_id입력하세요");
        params.add("redirect_uri", "http://localhost:9000/callback"); //등록한 redirect uri
        params.add("code", code);

        // 3. header + body
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, httpHeaders);

        // 4. http 요청하기
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                httpEntity,
                Object.class
        );

        System.out.println("response = " + response);


        return "home";
    }
}
