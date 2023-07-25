package com.umc.drawmap.repository;

import com.umc.drawmap.domain.User;
import com.umc.drawmap.domain.UserChallenge;
import com.umc.drawmap.domain.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUserChallenge(UserChallenge userChallenge);

    User findUserByUserCourses(UserCourse UserCourse);

    Optional<User> findByNickName(String nickName);

    Boolean existsByEmail(String email);
}
