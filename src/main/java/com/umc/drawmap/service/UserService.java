package com.umc.drawmap.service;

import java.util.ArrayList;

import com.umc.drawmap.dto.token.TokenResDto;
import com.umc.drawmap.security.jwt.JwtProvider;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;
import com.umc.drawmap.domain.User;
import com.umc.drawmap.dto.user.UserReqDto;
import com.umc.drawmap.dto.user.UserResDto;
import com.umc.drawmap.exception.user.DuplicateUserEmailException;
import com.umc.drawmap.exception.user.DuplicateUserNickNameException;
import com.umc.drawmap.exception.userChallenge.NoExistUserException;
import com.umc.drawmap.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;
    public UserService(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
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

    // 유저 닉네임 중복체크 => Service 부분에서는 Boolean 으로 변경하는게 좋을 것 같다.
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

    // 이메일 중복 체크 => Service 부분에서는 Boolean 으로 변경하는게 좋을 것 같다.
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



}
