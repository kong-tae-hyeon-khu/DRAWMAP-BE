package com.umc.drawmap.service.security;

import com.umc.drawmap.domain.Role;
import com.umc.drawmap.domain.User;
import com.umc.drawmap.dto.token.TokenReqDto;
import com.umc.drawmap.dto.token.TokenResDto;
import com.umc.drawmap.dto.user.UserReqDto;
import com.umc.drawmap.dto.user.UserResDto;
import com.umc.drawmap.exception.user.DuplicateUserEmailException;
import com.umc.drawmap.exception.userChallenge.NoExistUserException;
import com.umc.drawmap.repository.UserRepository;
import com.umc.drawmap.security.KakaoAccount;
import com.umc.drawmap.security.KakaoUserInfo;
import com.umc.drawmap.security.KakaoUserInfoResponse;
import com.umc.drawmap.security.jwt.JwtProvider;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final KakaoUserInfo kakaoUserInfo;

    private final JwtProvider jwtProvider;
    private final RedisTemplate redisTemplate;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public CustomOAuth2UserService(UserRepository userRepository, JwtProvider jwtProvider, KakaoUserInfo kakaoUserInfo,
                                   RedisTemplate redisTemplate, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.kakaoUserInfo = kakaoUserInfo;
        this.redisTemplate = redisTemplate;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }


    @Transactional
    public UserResDto.PostSignDto createUser(UserReqDto.signUpDto dto) {

        // 카카오 토큰으로부터 email 을 얻는 과정.
        String access_token = dto.getKakao_access_token();
        KakaoUserInfoResponse kakaoUserInfoResponse = kakaoUserInfo.getUserInfo(access_token);
        KakaoAccount kakaoAccount = kakaoUserInfoResponse.getKakao_account();
        String email = kakaoAccount.getEmail();

        if(userRepository.existsByEmail(email)) {
            throw new DuplicateUserEmailException(); // 해당 이메일의 유저가 존재.
        }
        String nickName = dto.getNickName();
        if(dto.getNickName() !=null){
            nickName = kakaoUserInfoResponse.getProperties().getNickname();
        }
        User user = User.builder()
                .nickName(nickName)
                .email(email)
                .gender(dto.getGender())
                .role(Role.ROLE_User) // 기본으로 User 로 지정.
                .sgg(dto.getSgg())
                .sido(dto.getSido())
                .bike(dto.getBike())
                .profileImg(dto.getProfileImg())
                .build();

        userRepository.save(user);

        return UserResDto.PostSignDto.builder()
                .accessToken(access_token)
                .nickName(user.getNickName())
                .bike(user.getBike())
                .sgg(user.getSgg())
                .sido(user.getSido())
                .email(user.getEmail())
                .role(user.getRole())
                .profileImg(user.getProfileImg())
                .tokenType("bearer")
                .build();
    }

    @Transactional
    public TokenResDto loginUser(TokenReqDto.accessReqDto tokenReqDto) {

        KakaoUserInfoResponse userInfo = kakaoUserInfo.getUserInfo(tokenReqDto.getAccess_token());
        KakaoAccount kakao_account = userInfo.getKakao_account();
        String email = kakao_account.getEmail();

        System.out.println("로그인 시도 하는 유저의 email : " + email);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, tokenReqDto.getAccess_token());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            // 유저에 대해 Access Token 을 줘야해.
            User user = userOptional.get();
            List<String> stringList = new ArrayList<>();
            stringList.add("User");
            TokenResDto tokenResDto = jwtProvider.createToken(user.getId(), stringList);
            redisTemplate.opsForValue().set("RT:" + authentication.getName(), tokenResDto.getRefreshToken(), tokenResDto.getRefreshTokenExpireDate(), TimeUnit.MILLISECONDS);
            return tokenResDto;
        }
        else {
            throw new NoExistUserException("해당 유저가 존재하지 않습니다. 회원가입이 필요합니다");
        }
    }

    // Token 재생성, 다시 저장
    public TokenResDto reissue(TokenReqDto.tokenReqDto request){
        if(!jwtProvider.validationToken(request.getRefresh_token())){
            throw new RuntimeException("Refresh Token 정보가 유효하지 않습니다.");
        }
        Authentication authentication = jwtProvider.getAuthentication(request.getAccess_token());
        String refreshToken = (String)redisTemplate.opsForValue().get("RT:" + authentication.getName());
        if(ObjectUtils.isEmpty(refreshToken)){
            throw new RuntimeException("잘못된 요청입니다.");
        }
        if(!refreshToken.equals(request.getRefresh_token())){
            throw new RuntimeException("Refresh Token 정보가 일치하지 않습니다.");
        }
        TokenResDto response = jwtProvider.Token(authentication);
        redisTemplate.opsForValue().set("RT:" + authentication.getName(), response.getRefreshToken(), response.getRefreshTokenExpireDate(), TimeUnit.MILLISECONDS);
        return response;

    }

    public void logoutUser(TokenReqDto.tokenReqDto token){
        if(!jwtProvider.validationToken(token.getAccess_token())){
            throw new RuntimeException("잘못된 요청입니다.");
        }
        Authentication authentication = jwtProvider.getAuthentication(token.getAccess_token());
        if(redisTemplate.opsForValue().get("RT:"+ authentication.getName())!=null){
            redisTemplate.delete("RT:" +authentication.getName());
        }
        Long expiration = jwtProvider.getExpiration(token.getAccess_token());
        redisTemplate.opsForValue().set(token.getAccess_token(), "logout", expiration, TimeUnit.MILLISECONDS);
    }
}
