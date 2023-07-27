package com.umc.drawmap.repository;

import com.umc.drawmap.domain.Challenge;
import com.umc.drawmap.domain.User;
import com.umc.drawmap.domain.UserCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
    Optional<UserCourse> findById(Long uCourseId);

    List<UserCourse> findAll();

    List<UserCourse> findAllByUser(User user);

    Page<UserCourse> findAll(Pageable pageable);

    Page<UserCourse> findAllBySidoOrSgg(String sido, String sgg, Pageable pageable);

}