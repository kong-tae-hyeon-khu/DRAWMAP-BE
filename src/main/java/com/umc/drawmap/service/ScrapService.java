package com.umc.drawmap.service;

import com.umc.drawmap.domain.Challenge;
import com.umc.drawmap.domain.Scrap;
import com.umc.drawmap.domain.User;
import com.umc.drawmap.domain.UserCourse;
import com.umc.drawmap.dto.scrap.ScrapReqDto;
import com.umc.drawmap.dto.scrap.ScrapResDto;
import com.umc.drawmap.exception.scrap.DupScrapException;
import com.umc.drawmap.exception.scrap.NoExistScrapException;
import com.umc.drawmap.exception.scrap.NoExistUserOrCourseException;
import com.umc.drawmap.repository.ChallengeRepository;
import com.umc.drawmap.repository.ScrapRepository;
import com.umc.drawmap.repository.UserCourseRepository;
import com.umc.drawmap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public ScrapResDto.ScrapDto addUserCourseScrap(ScrapReqDto.ScrapAddDto dto) {
        Optional<User> userOptional = userRepository.findById(dto.getUser_id());
        Optional<UserCourse> userCourseOptional = userCourseRepository.findById(dto.getUser_course_id());

        if (!userOptional.isPresent() || !userCourseOptional.isPresent()) {
            throw new NoExistUserOrCourseException("유저 또는 코스의 아이디가 존재하지 않습니다.");
        }
        User user = userOptional.get();
        UserCourse userCourse = userCourseOptional.get();

        if (scrapRepository.findByUserAndUserCourse(user, userCourse).isPresent()) {
            throw new DupScrapException("이미 해당 코스에 대해서 스크랩을 하셨습니다.");
        }
        Scrap scrap = Scrap.builder()
                .user(user)
                .userCourse(userCourse)
                .build();
        userCourse.updateCount(userCourse.getScrapCount() + 1);
        scrapRepository.save(scrap);
        userCourseRepository.save(userCourse);
        ScrapResDto.ScrapDto resDto = ScrapResDto.ScrapDto.builder()
                .user_courseId(dto.getUser_course_id())
                .userId(dto.getUser_id())
                .message("스크랩 성공")
                .build();

        return resDto;


    }

    public ScrapResDto.ScrapDto addChallengeScrap(ScrapReqDto.ScrapAddDto dto) {
        Optional<User> userOptional = userRepository.findById(dto.getUser_id());
        Optional<Challenge> challengeOptional = challengeRepository.findById(dto.getChallenge_id());

        if (!userOptional.isPresent() || !challengeOptional.isPresent()) {
            throw new NoExistUserOrCourseException("해당 유저 또는 코스의 아이디가 존재하지 않습니다.");
        }


        User user = userOptional.get();
        Challenge challenge = challengeOptional.get();

        if (scrapRepository.findByUserAndChallenge(user, challenge).isPresent()) {
            throw new DupScrapException("이미 해당 코스에 대해서 스크랩을 하셨습니다.");
        }

        Scrap scrap = Scrap.builder()
                .user(user)
                .challenge(challenge)
                .build();

        challenge.updateCount(challenge.getScrapCount() + 1);
        scrapRepository.save(scrap);
        challengeRepository.save(challenge);

        ScrapResDto.ScrapDto resDto = ScrapResDto.ScrapDto.builder()
                .userId(dto.getUser_id())
                .challengeId(dto.getChallenge_id())
                .message("스크랩 성공")
                .build();

        return resDto;
    }


    public ScrapResDto.ScrapListDto getMyScrap(Long userId) {

        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new NoExistUserOrCourseException("해당 유저가 존재하지 않습니다.");
        }
        List<Scrap> scrapList = scrapRepository.findAllByUser(user.get());

        List<ScrapResDto.BaseDto> baseDtoList = new ArrayList<>();

        for (Scrap scrap : scrapList) {
            ScrapResDto.BaseDto scrapDto;
            if (scrap.getChallenge() == null) {
                scrapDto = ScrapResDto.BaseDto.builder()
                        .user_courseId(scrap.getUserCourse().getId())
                        .userId(userId)
                        .build();

                baseDtoList.add(scrapDto);
            }
            else {
                scrapDto = ScrapResDto.BaseDto.builder()
                        .userId(userId)
                        .challengeId(scrap.getChallenge().getId())
                        .build();

                baseDtoList.add(scrapDto);
            }
        }
        ScrapResDto.ScrapListDto resDto = ScrapResDto.ScrapListDto.builder()
                .baseDtoList(baseDtoList)
                .message("내가 스크랩한 코스 목록")
                .build();
        return resDto;

    }

    public ScrapResDto.ScrapDto deleteMyScrap(Long userId, Long courseId, Long challengeId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (courseId == null) {
            Optional<Challenge> challengeOptional = challengeRepository.findById(challengeId);

            if (!userOptional.isPresent() || !challengeOptional.isPresent()) {
                throw new NoExistUserOrCourseException("해당 유저 또는 코스의 아이디가 존재하지 않습니다.");
            }
            User user = userOptional.get();
            Challenge challenge = challengeOptional.get();
            Optional<Scrap> scrapOptional = scrapRepository.findByUserAndChallenge(user, challenge);
            if (!scrapOptional.isPresent()) {
                throw new NoExistScrapException();
            }
            Scrap scrap = scrapOptional.get();
            challenge.updateCount(challenge.getScrapCount() - 1);
            challengeRepository.save(challenge);
            scrapRepository.delete(scrap);
            ScrapResDto.ScrapDto scrapDto = ScrapResDto.ScrapDto.builder()
                    .challengeId(challengeId)
                    .userId(userId)
                    .message("스크랩 삭제 성공")
                    .build();
            return scrapDto;

        } else {
            Optional<UserCourse> userCourseOptional = userCourseRepository.findById(courseId);
            if (!userOptional.isPresent() || !userCourseOptional.isPresent()) {
                throw new NoExistUserOrCourseException("해당 유저 또는 코스의 아이디가 존재하지 않습니다.");
            }
            User user = userOptional.get();
            UserCourse userCourse = userCourseOptional.get();
            Optional<Scrap> scrapOptional = scrapRepository.findByUserAndUserCourse(user, userCourse);
            if (!scrapOptional.isPresent()) {
                throw new NoExistScrapException();
            }
            Scrap scrap = scrapOptional.get();
            userCourse.updateCount(userCourse.getScrapCount() - 1);
            userCourseRepository.save(userCourse);
            scrapRepository.delete(scrap);
            ScrapResDto.ScrapDto scrapDto = ScrapResDto.ScrapDto.builder()
                    .user_courseId(courseId)
                    .userId(userId)
                    .message("스크랩 삭제 성공")
                    .build();
            return scrapDto;
        }
    }

    public Boolean findScrapByUserAndUserCourse(User user, UserCourse userCourse){
        return scrapRepository.existsScrapByUserAndUserCourse(user, userCourse);
    }
}
