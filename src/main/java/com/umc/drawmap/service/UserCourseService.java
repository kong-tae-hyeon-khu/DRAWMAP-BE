package com.umc.drawmap.service;

import com.umc.drawmap.domain.Challenge;
import com.umc.drawmap.domain.User;
import com.umc.drawmap.domain.UserCourse;
import com.umc.drawmap.dto.UserCourseReqDto;
import com.umc.drawmap.dto.UserResDto;
import com.umc.drawmap.exception.NotFoundException;
import com.umc.drawmap.repository.ChallengeRepository;
import com.umc.drawmap.repository.UserCourseRepository;
import com.umc.drawmap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCourseService {
    private final UserCourseRepository userCourseRepository;

    private final UserRepository userRepository;

    private final ChallengeRepository challengeRepository;
    @Transactional
    public UserCourse create(List<MultipartFile> files, UserCourseReqDto.CreateUserCourseDto request) throws IOException {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(()-> new NotFoundException("유저를 찾을 수 없습니다."));

        UserCourse userCourse = UserCourse.builder()
                .userCourseTitle(request.getUserCourseTitle())
                .sido(request.getSido())
                .sgg(request.getSgg())
                .userCourseContent(request.getUserCourseContent())
                .userCourseComment(request.getUserCourseComment())
                .userCourseDifficulty(request.getUserCourseDifficulty())
                .userImage(FileService.fileUpload(files))
                .user(user)
                .build();

        return userCourseRepository.save(userCourse);
    }
    @Transactional
    public UserCourse update(Long uCourseId, List<MultipartFile> files, UserCourseReqDto.UpdateUserCourseDto request) throws IOException{
        UserCourse userCourse = userCourseRepository.findById(uCourseId)
                .orElseThrow(()-> new NotFoundException("유저개발코스를 찾을 수 없습니다."));
        userCourse.update(request.getUserCourseTitle(), request.getSido(), request.getSgg(),
                request.getUserCourseDifficulty(), request.getUserCourseContent(),
                request.getUserCourseComment(), FileService.fileUpload(files));
        return userCourse;
    }

    public void delete(Long ucourseId){
        userCourseRepository.deleteById(ucourseId);
    }

    // 유저 도전 조회
    public List<UserCourse> userCourseList() {
        return userCourseRepository.findAll();
    }


    // 유저코스 10개씩
    public Page<UserCourse> getPage(int page){
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        return userCourseRepository.findAll(pageRequest);
    }

    public Page<UserCourse> getPageByScrap(int page){
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "scrapCount"));
        return userCourseRepository.findAll(pageRequest);
    }

    public Page<UserCourse> getPageByArea(int page, String sido, String sgg){
        PageRequest pageRequest = PageRequest.of(page, 10);
        return userCourseRepository.findAllBySidoOrSgg(sido, sgg, pageRequest);
    }

    public List<UserCourse> findAllByUser(Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
        return userCourseRepository.findAllByUser(user);
    }

    public UserCourse findById(Long uCourseId){
        return userCourseRepository.findById(uCourseId).get();
    }

    public List<UserResDto.UserDto> getTop3User(){
        List<User> userList = userRepository.findAll();
        int scrap1 = 0;
        int scrap2 = 0;
        int scrap3 = 0;
        User user1 = null;
        User user2 = null;
        User user3 = null;

        for(User u: userList){
            List<UserCourse> userCourseList = userCourseRepository.findAllByUser(u);
            int localscrap = 0;
            for(UserCourse c: userCourseList){
                localscrap += c.getScrapCount();
            }
            if(localscrap > scrap1){
                scrap1 = localscrap;
                user1 = u;
            }else if (localscrap > scrap2){
                scrap2 = localscrap;
                user2 = u;
            }else if (localscrap > scrap3){
                scrap3 = localscrap;
                user3 = u;
            }
        }
        List<User> topUserList = new ArrayList<>();
        topUserList.add(user1);
        topUserList.add(user2);
        topUserList.add(user3);
        List<UserResDto.UserDto> list = new ArrayList<>();
        for(User u : topUserList){
            UserResDto.UserDto userDto = UserResDto.UserDto.builder()
                    .userId(u.getId())
                    .nickName(u.getNickName())
                    .profileImg(u.getProfileImg())
                    .build();
            list.add(userDto);
        }
        return list;

    }

}
