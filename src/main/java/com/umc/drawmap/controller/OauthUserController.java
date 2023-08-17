package com.umc.drawmap.controller;

import com.umc.drawmap.dto.token.TokenReqDto;
import com.umc.drawmap.dto.token.TokenResDto;
import com.umc.drawmap.dto.user.UserReqDto;
import com.umc.drawmap.dto.user.UserResDto;
import com.umc.drawmap.exception.BaseResponse;

import com.umc.drawmap.service.security.CustomOAuth2UserService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController

public class OauthUserController {

    private final CustomOAuth2UserService customOAuth2UserService;

    public OauthUserController(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    // 카카오 토큰을 가져 오는 과정을 하나로 합쳐 두었습니다.
    @ResponseBody
    @GetMapping("/callback")
    public String kakaoCallback(@RequestParam String code) throws ParseException {
        System.out.println(code);
        // 1. header 생성
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

        // 2. body 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code"); //고정값
        params.add("client_id", ""); // Client-id 입력해주세요!
        params.add("redirect_uri", "http://localhost:9000/callback"); //등록한 redirect uri
        params.add("code", code);

        // 3. header + body
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, httpHeaders);

        // 4. http 요청하기
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        System.out.println("response = " + response);

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody());
        String accessToken = (String) jsonObject.get("access_token");
        String refreshToken = (String) jsonObject.get("refresh_token");
        Map<String, String> token = new LinkedHashMap<>();
        token.put("access_token", accessToken);
        token.put("refresh_token", refreshToken);

        return token.toString();
    }

    // KaKao Access Token => 로그인 => JWT 발급.
    @PostMapping("/user/login")
    public BaseResponse<TokenResDto> loginUser(@RequestBody TokenReqDto.accessReqDto tokenReqDto) {
        TokenResDto tokenResDto = customOAuth2UserService.loginUser(tokenReqDto);
        return new BaseResponse<>(tokenResDto);
    }

    @PostMapping("/user/signup")
    public BaseResponse<UserResDto.PostSignDto> signUp(@RequestBody UserReqDto.signUpDto signUpDto) {
        UserResDto.PostSignDto user = customOAuth2UserService.createUser(signUpDto);
        return new BaseResponse<>(user);
    }

    @PostMapping("/user/logout")
    public BaseResponse<String> logoutUser(@RequestBody TokenReqDto.tokenReqDto token){
        customOAuth2UserService.logoutUser(token);
        return new BaseResponse<>("로그아웃이 완료되었습니다.");
    }

    @PostMapping("/reissue")
    public BaseResponse<TokenResDto> reissueToken(@RequestBody TokenReqDto.tokenReqDto token){
        return new BaseResponse<>(customOAuth2UserService.reissue(token));
    }


}
