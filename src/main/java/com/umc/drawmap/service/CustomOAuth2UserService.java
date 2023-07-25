package com.umc.drawmap.service;


import com.umc.drawmap.domain.User;
import com.umc.drawmap.dto.user.UserResDto;
import com.umc.drawmap.exception.user.DuplicateUserEmailException;
import com.umc.drawmap.exception.user.DuplicateUserNickNameException;
import com.umc.drawmap.exception.userChallenge.NoExistUserException;
import com.umc.drawmap.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService {
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



}
