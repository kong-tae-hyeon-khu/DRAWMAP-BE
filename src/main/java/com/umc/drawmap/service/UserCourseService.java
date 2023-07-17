package com.umc.drawmap.service;

import com.umc.drawmap.domain.UserCourse;
import com.umc.drawmap.dto.UserCourseReqDto;
import com.umc.drawmap.repository.UserCourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCourseService {
    private final UserCourseRepository userCourseRepository;
    @Transactional
    public UserCourse create(List<MultipartFile> files, UserCourseReqDto.CreateUserCourseDto request) throws IOException {
        UserCourse userCourse = UserCourse.builder()
                .userCourseTitle(request.getUserCourseTitle())
                .userCourseArea(request.getUserCourseArea())
                .userCourseContent(request.getUserCourseContent())
                .userCourseDifficulty(request.getUserCourseDifficulty())
                .userImage(FileService.fileUpload(files))
                .build();

        return userCourseRepository.save(userCourse);
    }
    @Transactional
    public UserCourse update(Long uCourseId, List<MultipartFile> files, UserCourseReqDto.UpdateUserCourseDto request) throws IOException{
        UserCourse userCourse = userCourseRepository.findById(uCourseId).get();
        userCourse.update(request.getUserCourseTitle(), request.getUserCourseArea(),
                request.getUserCourseDifficulty(), request.getUserCourseContent(),
                FileService.fileUpload(files));
        return userCourse;
    }

    public void delete(Long ucourseId){
        userCourseRepository.deleteById(ucourseId);
    }

    // 유저 도전 조회
    public List<UserCourse> userCourseList() {
        return userCourseRepository.findAll();
    }

}
