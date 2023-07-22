package com.umc.drawmap.dto;

import com.umc.drawmap.domain.User;
import com.umc.drawmap.domain.UserCourse;
import com.umc.drawmap.repository.UserRepository;
import com.umc.drawmap.service.ScrapService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserCourseConverter {

    private static UserRepository userRepository;
    private static ScrapService scrapService;

    public static UserCourseResDto.UserCourseDto toUserCourseDto(UserCourse userCourse){

        User user = userRepository.findUserByUserCourses(userCourse);
        Boolean isScraped = scrapService.findScrapByUserAndUserCourse(user, userCourse);

        return UserCourseResDto.UserCourseDto.builder()
                .title(userCourse.getUserCourseTitle())
                .userCourseId(userCourse.getId())
                .content(userCourse.getUserCourseContent())
                .area(userCourse.getUserCourseArea())
                .image(userCourse.getUserImage())
                .createdDate(userCourse.getCreatedAt())
                .difficulty(userCourse.getUserCourseDifficulty())
                .isScraped(isScraped)
                .user(UserResDto.UserDto.builder().userId(user.getId()).profileImg(user.getProfileImg()).nickName(user.getNickName()).build())
                .scrapCount(userCourse.getScrapCount())
                .build();
    }

    public static List<UserCourseResDto.UserCourseDto> toUserCourseDtoList(List<UserCourse> userCourseList){
        return userCourseList.stream()
                .map(userCourse -> toUserCourseDto(userCourse))
                .collect(Collectors.toList());
    }

    public static UserCourseResDto.MyUserCourseDto toMyUserCourseDto(UserCourse userCourse){
        User user = userRepository.findUserByUserCourses(userCourse);

        return UserCourseResDto.MyUserCourseDto.builder()
                .userCourseId(userCourse.getId())
                .area(userCourse.getUserCourseArea())
                .image(userCourse.getUserImage())
                .createdDate(userCourse.getCreatedAt())
                .user(UserResDto.UserDto.builder().userId(user.getId()).profileImg(user.getProfileImg()).nickName(user.getNickName()).build())
                .build();
    }

    public static List<UserCourseResDto.MyUserCourseDto> toUserCourseDtoMyList(List<UserCourse> userCourseList){
        return userCourseList.stream()
                .map(userCourse -> toMyUserCourseDto(userCourse))
                .collect(Collectors.toList());
    }

    public static List<UserCourseResDto.UserCourseSortDto> toUserCourseSortDto(List<UserCourse> userCourseList){
        List<UserCourseResDto.UserCourseSortDto> sortList = new ArrayList<>();
        for(UserCourse u: userCourseList){
            User user = u.getUser();
            UserCourseResDto.UserCourseSortDto result = UserCourseResDto.UserCourseSortDto.builder()
                    .userCourseId(u.getUserId())
                    .title(u.getUserCourseTitle())
                    .difficulty(u.getUserCourseDifficulty())
                    .content(u.getUserCourseContent())
                    .area(u.getUserCourseArea())
                    .user(UserResDto.UserDto.builder().userId(user.getId()).nickName(user.getNickName()).profileImg(user.getProfileImg()).build())
                    .image(u.getUserImage())
                    .createdDate(u.getCreatedAt())
                    .build();
            sortList.add(result);
        }
        return sortList;
    }


}
