package com.umc.drawmap.service;

import com.umc.drawmap.domain.Challenge;
import com.umc.drawmap.domain.Scrap;
import com.umc.drawmap.domain.User;
import com.umc.drawmap.domain.UserCourse;
import com.umc.drawmap.dto.scrap.ScrapReqDto;
import com.umc.drawmap.dto.scrap.ScrapResDto;
import com.umc.drawmap.repository.ChallengeRepository;
import com.umc.drawmap.repository.ScrapRepository;
import com.umc.drawmap.repository.UserCourseRepository;
import com.umc.drawmap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;
    private final UserCourseRepository userCourseRepository;
    private final ChallengeRepository challengeRepository;

    public Boolean findScrapByUserAndChallenge(User user, Challenge challenge){
        return scrapRepository.existsScrapByUserAndChallenge(user, challenge);
    }

    // User - Scrap
    public String addUserCourseScrap(ScrapReqDto.ScrapAddDto dto) {
        Optional<User> userOptional = userRepository.findById(dto.getUser_id());
        Optional<UserCourse> userCourseOptional = userCourseRepository.findById(dto.getUser_course_id());

        if (!userOptional.isPresent() || !userCourseOptional.isPresent()) {
            return "해당 유저 또는 코스가 존재하지 않습니다.";
        }
        User user = userOptional.get();
        UserCourse userCourse = userCourseOptional.get();

        if (scrapRepository.findByUserAndUserCourse(user, userCourse).isPresent()) {
            return "이미 스크랩을 하셨습니다.";
        }
        Scrap scrap = Scrap.builder()
                .user(user)
                .userCourse(userCourse)
                .build();
        userCourse.updateCount(userCourse.getScrapCount() + 1);
        scrapRepository.save(scrap);
        userCourseRepository.save(userCourse);

        return "스크랩 성공";

    }

    public String addChallengeScrap(ScrapReqDto.ScrapAddDto dto) {
        Optional<User> userOptional = userRepository.findById(dto.getUser_id());
        Optional<Challenge> challengeOptional = challengeRepository.findById(dto.getChallenge_id());

        if (!userOptional.isPresent() || !challengeOptional.isPresent()) {
            return "해당 유저 또는 코스가 존재하지 않습니다.";
        }


        User user = userOptional.get();
        Challenge challenge = challengeOptional.get();

        if (scrapRepository.findByUserAndChallenge(user, challenge).isPresent()) {
            return "이미 스크랩을 하셨습니다.";
        }

        Scrap scrap = Scrap.builder()
                .user(user)
                .challenge(challenge)
                .build();

        challenge.updateCount(challenge.getScrapCount() + 1);
        scrapRepository.save(scrap);
        challengeRepository.save(challenge);
        return "스크랩 성공";
    }

    public ResponseEntity<List<ScrapResDto.ScrapDto>> getMyScrap(Long userId) {

        Optional<User> user = userRepository.findById(userId);
        List<Scrap> scrapList = scrapRepository.findAllByUser(user.get());

        List<ScrapResDto.ScrapDto> scrapDtoList = new ArrayList<>();

        for (Scrap scrap : scrapList) {
            ScrapResDto.ScrapDto scrapDto;
            if (scrap.getChallenge() == null) {
                scrapDto = ScrapResDto.ScrapDto.builder()
                        .user_courseId(scrap.getUserCourse().getId())
                        .build();
            }
            else {
                scrapDto = ScrapResDto.ScrapDto.builder()
                        .challengeId(scrap.getChallenge().getId())
                        .build();
            }
            scrapDtoList.add(scrapDto);
        }

        return ResponseEntity.ok(scrapDtoList);
    }
}
