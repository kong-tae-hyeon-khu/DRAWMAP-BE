package com.umc.drawmap.service;

import com.umc.drawmap.domain.User;
import com.umc.drawmap.domain.UserCourse;
import com.umc.drawmap.dto.UserCourseReqDto;
import com.umc.drawmap.dto.UserCourseResDto;
import com.umc.drawmap.dto.user.UserResDto;
import com.umc.drawmap.exception.NotFoundException;
import com.umc.drawmap.repository.ScrapRepository;
import com.umc.drawmap.repository.UserCourseRepository;
import com.umc.drawmap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCourseService {
    private final UserCourseRepository userCourseRepository;

    private final UserRepository userRepository;

    private final ScrapRepository scrapRepository;
    private final S3FileService s3FileService;
    @Transactional
    public UserCourse create(List<MultipartFile> files, UserCourseReqDto.CreateUserCourseDto request) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new NotFoundException("유저가 존재하지 않습니다."));

        UserCourse userCourse = UserCourse.builder()
                .userCourseTitle(request.getUserCourseTitle())
                .sido(request.getSido())
                .sgg(request.getSgg())
                .userCourseContent(request.getUserCourseContent())
                .userCourseComment(request.getUserCourseComment())
                .userCourseDifficulty(request.getUserCourseDifficulty())
                .userImage(s3FileService.upload(files))
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
                request.getUserCourseComment(), s3FileService.upload(files));
        return userCourse;
    }

    public void delete(Long ucourseId){
        userCourseRepository.deleteById(ucourseId);
    }

    // 유저 도전 조회
    public List<UserCourseResDto.UserCourseDto> userCourseList() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new NotFoundException("유저가 존재하지 않습니다."));

        List<UserCourse> userCourseList = userCourseRepository.findAll();

        List<UserCourseResDto.UserCourseDto> list = new ArrayList<>();
        for(UserCourse userCourse : userCourseList){
            Boolean isMyPost = userCourseRepository.existsByUser(user);
            Boolean isScraped = scrapRepository.existsScrapByUserAndUserCourse(user, userCourse);
            UserCourseResDto.UserCourseDto result = UserCourseResDto.UserCourseDto.builder()
                    .userCourseId(userCourse.getId())
                    .title(userCourse.getUserCourseTitle())
                    .comment(userCourse.getUserCourseComment())
                    .sgg(userCourse.getSgg())
                    .sido(userCourse.getSido())
                    .isMyPost(isMyPost)
                    .isScraped(isScraped)
                    .user(UserResDto.UserDto.builder().userId(userCourse.getUser().getId()).profileImg(userCourse.getUser().getProfileImg()).nickName(userCourse.getUser().getNickName()).build())
                    .content(userCourse.getUserCourseContent())
                    .createdDate(userCourse.getCreatedAt())
                    .difficulty(userCourse.getUserCourseDifficulty())
                    .scrapCount(userCourse.getScrapCount())
                    .build();
            list.add(result);
        }
        return list;
    }


    // 유저코스 리스트
    public List<UserCourseResDto.UserCourseSortDto> getList(String sort){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new NotFoundException("유저가 존재하지 않습니다."));

        List<UserCourse> list;
        if(Objects.equals(sort, "scrapcount")){

            list = userCourseRepository.findAllByOrderByScrapCountDesc();
        } else {

            list = userCourseRepository.findAllByOrderByCreatedAtDesc();
        }

        List<UserCourseResDto.UserCourseSortDto> resultList = new ArrayList<>();
        for (UserCourse userCourse: list){
            Boolean isMyPost = userCourseRepository.existsByUser(user);
            Boolean isScraped= scrapRepository.existsScrapByUserAndUserCourse(user, userCourse);
            UserCourseResDto.UserCourseSortDto result = UserCourseResDto.UserCourseSortDto.builder()
                    .userCourseId(userCourse.getId())
                    .sido(userCourse.getSido())
                    .sgg(userCourse.getSgg())
                    .isMyPost(isMyPost)
                    .isScraped(isScraped)
                    .user(UserResDto.UserDto.builder().userId(userCourse.getUser().getId()).nickName(userCourse.getUser().getNickName()).profileImg(userCourse.getUser().getProfileImg()).build())
                    .title(userCourse.getUserCourseTitle())
                    .content(userCourse.getUserCourseContent())
                    .comment(userCourse.getUserCourseComment())
                    .image(userCourse.getUserImage())
                    .createdDate(userCourse.getCreatedAt())
                    .difficulty(userCourse.getUserCourseDifficulty())
                    .build();
            resultList.add(result);
        }
        return resultList;

    }

    public List<UserCourseResDto.UserCourseSortDto> getListByArea(String sido, String sgg){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new NotFoundException("유저가 존재하지 않습니다."));

        List<UserCourse> list = userCourseRepository.findAllBySidoOrSgg(sido, sgg);

        List<UserCourseResDto.UserCourseSortDto> resultList = new ArrayList<>();
        for (UserCourse userCourse: list){
            Boolean isMyPost = userCourseRepository.existsByUser(user);
            Boolean isScraped= scrapRepository.existsScrapByUserAndUserCourse(user, userCourse);
            UserCourseResDto.UserCourseSortDto result = UserCourseResDto.UserCourseSortDto.builder()
                    .userCourseId(userCourse.getId())
                    .sido(userCourse.getSido())
                    .sgg(userCourse.getSgg())
                    .isMyPost(isMyPost)
                    .isScraped(isScraped)
                    .user(UserResDto.UserDto.builder().userId(userCourse.getUser().getId()).nickName(userCourse.getUser().getNickName()).profileImg(userCourse.getUser().getProfileImg()).build())
                    .title(userCourse.getUserCourseTitle())
                    .content(userCourse.getUserCourseContent())
                    .comment(userCourse.getUserCourseComment())
                    .image(userCourse.getUserImage())
                    .createdDate(userCourse.getCreatedAt())
                    .difficulty(userCourse.getUserCourseDifficulty())
                    .build();
            resultList.add(result);
        }
        return resultList;
    }

    public List<UserCourseResDto.MyUserCourseDto> findAllByUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new NotFoundException("유저가 존재하지 않습니다."));

        List<UserCourse> list = userCourseRepository.findAllByUser(user);

        List<UserCourseResDto.MyUserCourseDto> resultList = new ArrayList<>();
        for(UserCourse userCourse: list){
            UserCourseResDto.MyUserCourseDto result = UserCourseResDto.MyUserCourseDto.builder()
                    .userCourseId(userCourse.getId())
                    .sido(userCourse.getSido())
                    .sgg(userCourse.getSgg())
                    .image(userCourse.getUserImage())
                    .createdDate(userCourse.getCreatedAt())
                    .user(UserResDto.UserDto.builder().userId(userCourse.getUser().getId()).nickName(userCourse.getUser().getNickName()).profileImg(userCourse.getUser().getProfileImg()).build())
                    .build();
            resultList.add(result);
        }
        return resultList;

    }

    public UserCourseResDto.UserCourseDto findById(Long uCourseId){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new NotFoundException("유저가 존재하지 않습니다."));

        UserCourse userCourse = userCourseRepository.findById(uCourseId)
                .orElseThrow(()-> new NotFoundException("유저코스를 찾을 수 없습니다."));

        Boolean isMyPost = userCourseRepository.existsByUser(user);
        Boolean isScraped = scrapRepository.existsScrapByUserAndUserCourse(user, userCourse);

        return UserCourseResDto.UserCourseDto.builder()
                .title(userCourse.getUserCourseTitle())
                .userCourseId(userCourse.getId())
                .content(userCourse.getUserCourseContent())
                .comment(userCourse.getUserCourseComment())
                .sido(userCourse.getSido())
                .sgg(userCourse.getSgg())
                .isScraped(isScraped)
                .isMyPost(isMyPost)
                .image(userCourse.getUserImage())
                .createdDate(userCourse.getCreatedAt())
                .difficulty(userCourse.getUserCourseDifficulty())
                .user(UserResDto.UserDto.builder().userId(userCourse.getUser().getId()).nickName(userCourse.getUser().getNickName()).profileImg(userCourse.getUser().getProfileImg()).build())
                .scrapCount(userCourse.getScrapCount())
                .build();
    }

    public List<UserResDto.UserDto> getTop3User(){
        List<User> userList = userRepository.findAll();
        int scrap1 = 0;
        int scrap2 = 0;
        int scrap3 = 0;

        // 3명 이하일 경우 현재까지 있는 유저 다 보내기
        if(userRepository.count() <= 3){
            List<UserResDto.UserDto> lessThan3UserList = new ArrayList<>();
            int totalScrap=0;
            int loaclScrap=0;
            for(User user: userList){
                List<UserCourse> userCourseList = userCourseRepository.findAllByUser(user);

                for(UserCourse userCourse: userCourseList){
                    totalScrap += userCourse.getScrapCount();
                }
                UserResDto.UserDto result = UserResDto.UserDto.builder()
                        .userId(user.getId())
                        .nickName(user.getNickName())
                        .profileImg(user.getProfileImg())
                        .build();
                if(totalScrap> loaclScrap){
                    lessThan3UserList.add(0, result);
                } else{
                    lessThan3UserList.add(result);
                }
                loaclScrap = totalScrap;
            }
            return lessThan3UserList;
        }

        User user1 = userRepository.findById(1L).orElseThrow(()-> new NotFoundException("유저를 찾을 수 없습니다."));
        User user2 = userRepository.findById(1L).orElseThrow(()-> new NotFoundException("유저를 찾을 수 없습니다."));
        User user3 = userRepository.findById(1L).orElseThrow(()-> new NotFoundException("유저를 찾을 수 없습니다."));

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
