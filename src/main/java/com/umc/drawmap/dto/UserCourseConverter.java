package com.umc.drawmap.dto;

import com.umc.drawmap.domain.User;
import com.umc.drawmap.domain.UserCourse;
import java.util.ArrayList;
import java.util.List;

public class UserCourseConverter {

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
