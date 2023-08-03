package com.umc.drawmap.service.security;

import com.umc.drawmap.domain.Role;
import com.umc.drawmap.domain.User;
import com.umc.drawmap.dto.token.TokenReqDto;
import com.umc.drawmap.dto.token.TokenResDto;
import com.umc.drawmap.dto.user.UserReqDto;
import com.umc.drawmap.exception.user.DuplicateUserEmailException;
import com.umc.drawmap.exception.userChallenge.NoExistUserException;
import com.umc.drawmap.repository.UserRepository;
import com.umc.drawmap.security.KakaoAccount;
import com.umc.drawmap.security.KakaoUserInfo;
import com.umc.drawmap.security.KakaoUserInfoResponse;
import com.umc.drawmap.security.jwt.JwtProvider;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final KakaoUserInfo kakaoUserInfo;

    private final JwtProvider jwtProvider;
    public CustomOAuth2UserService(UserRepository userRepository, JwtProvider jwtProvider, KakaoUserInfo kakaoUserInfo) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.kakaoUserInfo = kakaoUserInfo;
    }


    @Transactional
    public User createUser(UserReqDto.signUpDto dto) {

        // 카카오 토큰으로부터 email 을 얻는 과정.
        String access_token = dto.getKakao_access_token();
        KakaoUserInfoResponse kakaoUserInfoResponse = kakaoUserInfo.getUserInfo(access_token);
        KakaoAccount kakaoAccount = kakaoUserInfoResponse.getKakao_account();
        String email = kakaoAccount.getEmail();

        if(userRepository.existsByEmail(email)) {
            throw new DuplicateUserEmailException(); // 해당 이메일의 유저가 존재.
        }
        User user = User.builder()
                .nickName(dto.getNickName())
                .email(email)
                .gender(dto.getGender())
                .role(Role.ROLE_User) // 기본으로 User 로 지정.
                .sgg(dto.getSgg())
                .sido(dto.getSido())
                .bike(dto.getBike())
                .profileImg(dto.getProfileImg())
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public TokenResDto loginUser(TokenReqDto tokenReqDto) {

        KakaoUserInfoResponse userInfo = kakaoUserInfo.getUserInfo(tokenReqDto.getAccess_token());
        KakaoAccount kakao_account = userInfo.getKakao_account();
        String email = kakao_account.getEmail();

        System.out.println("로그인 시도 하는 유저의 email : " + email);


        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            // 유저에 대해 Access Token 을 줘야해.
            User user = userOptional.get();
            List<String> stringList = new ArrayList<>();
            stringList.add("User");
            TokenResDto tokenResDto = jwtProvider.createToken(user.getId(), stringList);
            return tokenResDto;
        }
        else {
            throw new NoExistUserException("해당 유저가 존재하지 않습니다. 회원가입이 필요합니다");
        }
    }
}
