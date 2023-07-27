package com.umc.drawmap.repository;

import com.umc.drawmap.domain.User;
import com.umc.drawmap.domain.UserChallenge;
import com.umc.drawmap.domain.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUserChallenge(UserChallenge userChallenge);

    User findUserByUserCourses(UserCourse UserCourse);

    Optional<User> findByNickName(String nickName);
    List<User> findAll();



    Boolean existsByEmail(String email);
}
