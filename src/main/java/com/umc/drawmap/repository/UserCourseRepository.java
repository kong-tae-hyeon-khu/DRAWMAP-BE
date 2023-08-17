package com.umc.drawmap.repository;


import com.umc.drawmap.domain.User;
import com.umc.drawmap.domain.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
    Optional<UserCourse> findById(Long uCourseId);

    List<UserCourse> findAll();

    List<UserCourse> findAllByUser(User user);

    List<UserCourse> findAllBySidoOrSgg(String sido, String sgg);

    Boolean existsByUser(User user);

    List<UserCourse> findAllByOrderByCreatedAtDesc();
    List<UserCourse> findAllByOrderByScrapCountDesc();

}