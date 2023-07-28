package com.umc.drawmap.service;

import java.util.Collections;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import com.umc.drawmap.domain.User;
import com.umc.drawmap.dto.user.UserReqDto;
import com.umc.drawmap.dto.user.UserResDto;
import com.umc.drawmap.exception.user.DuplicateUserEmailException;
import com.umc.drawmap.exception.user.DuplicateUserNickNameException;
import com.umc.drawmap.exception.userChallenge.NoExistUserException;
import com.umc.drawmap.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 유저 기본 정보 조회.

    public UserResDto.UserDto getUserInfo(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (!userOptional.isPresent()) {
            throw new NoExistUserException("해당 아이디의 유저가 존재하지 않습니다.");
        }
        User user = userOptional.get();
        UserResDto.UserDto userDto = UserResDto.UserDto.builder()
                .userId(user.getId())
                .nickName(user.getNickName())
                .profileImg(user.getProfileImg())
                .build();
        return userDto;
    }

    // 유저 닉네임 중복체크
    public UserResDto.UserNameDto checkUserName(String nickname) {
        Optional<User> userOptional = userRepository.findByNickName(nickname);

        if (!userOptional.isPresent()) {
            UserResDto.UserNameDto userNameDto = UserResDto.UserNameDto.builder()
                    .nickName(nickname)
                    .message("해당 닉네임은 사용가능합니다.")
                    .build();
            return userNameDto;
        }
        else {
            throw new DuplicateUserNickNameException(); // 해당 닉네임을 가진 유저가 존재할 때.
        }
    }

    // 이메일 중복 체크
    public UserResDto.UserEmailDto checkUserEmail(String userEmail) {
        System.out.println(userEmail);
        if (userRepository.existsByEmail(userEmail)) {
            throw new DuplicateUserEmailException();
        }
        UserResDto.UserEmailDto userEmailDto = UserResDto.UserEmailDto.builder()
                .email(userEmail)
                .message("해당 이메일은 사용가능합니다.")
                .build();

        return userEmailDto;
    }

    // 유저 기본 정보 수정
    public UserResDto.UserDto updateUser(Long userId, UserReqDto.updateDto dto) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new NoExistUserException("해당 유저가 존재하지 않습니다.");
        }
        User user = userOptional.get();

        // 이메일 중복 검사.
        if (userRepository.findByNickName(dto.getNickName()).isPresent()) {
            throw new DuplicateUserNickNameException();
        }
        // 수정 정보 반영
        user.setNickName(dto.getNickName());
        user.setBike(dto.getBike());
        user.setProfileImg(dto.getProfileImg());
        user.setSido(dto.getSido());
        user.setSgg(dto.getSgg());

        userRepository.save(user);

        UserResDto.UserDto userDto = UserResDto.UserDto.builder()
                .userId(userId)
                .nickName(dto.getNickName())
                .profileImg(dto.getProfileImg())
                .build();

        return userDto;
    }

    @Transactional
    public User createUser(String email, UserReqDto.signUpDto dto) {
        // 이미 가입했는지 이메일을 통해 Check
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            throw new DuplicateUserEmailException();
        }
        // 가입하지 않았다면, -> 유저 생성.
        User user = User.builder()
                .nickName(dto.getNickName())
                .bike(dto.getBike())
                .role(dto.getRole())
                .gender(dto.getGender())
                .sgg(dto.getSgg())
                .sido(dto.getSido())
                .email(dto.getEmail())
                .profileImg(dto.getProfileImg())
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public User loginUser(String email) {
        // 우리의 Access Token (JWT) 를 발급해주어야 할까..?ㅎ
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        else {
            throw new NoExistUserException("해당 유저가 존재하지 않습니다. 회원가입이 필요합니다");
        }
    }


}
